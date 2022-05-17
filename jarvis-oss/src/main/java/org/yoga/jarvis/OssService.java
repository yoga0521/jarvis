/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.springframework.lang.Nullable;
import org.yoga.jarvis.beans.OssResourceDTO;
import org.yoga.jarvis.constants.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.utils.Assert;
import org.yoga.jarvis.utils.IOUtils;
import org.yoga.jarvis.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @Description: Oss operate service
 * @Author: yoga
 * @Date: 2022/5/13 14:29
 */
public class OssService {

    /**
     * Default folder
     */
    private static final String DEFAULT_FOLDER = "default";

    /**
     * Oss internal endpoint suffix
     */
    private static final String OSS_INTERNAL_ENDPOINT_SUFFIX = "-internal";

    /**
     * Minimum size of segment(100 * 1024kb)
     */
    private static final long MIN_SEGMENT_SIZE = 102_400;

    /**
     * Endpoint of oss
     */
    private String endpoint;

    /**
     * Bucket name of oss
     */
    private String bucketName;

    /**
     * accessKey ID of oss
     */
    private String accessKeyId;

    /**
     * accessKey Secret of oss
     */
    private String accessKeySecret;

    /**
     * Maximum size of resource, default 5 GB
     * If larger than this size, use multipart upload
     */
    private long resourceUploadMaxSize = 5 * 1024 * 1024;


    public OssService(String endpoint, String bucketName, String accessKeyId, String accessKeySecret, long resourceUploadMaxSize) {
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.resourceUploadMaxSize = resourceUploadMaxSize;
        // check param TODO
    }

    /**
     * upload resource to oss
     *
     * @param inputStream       resource
     * @param resourceName      name of resource
     * @param resourceSize      size of resource
     * @param objectName        objectName
     * @param requireFormat     Whether to require resource format
     * @param isShowProgressBar Whether to show progress bar
     * @return oss result {@link org.yoga.jarvis.beans.OssResourceDTO}
     */
    private OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, @Nullable String objectName,
                                  boolean requireFormat, boolean isShowProgressBar) throws IllegalArgumentException {
        Assert.isTrue(resourceSize < resourceUploadMaxSize, "the resource is too large, please use multipart upload!");
        Assert.notNull(inputStream, "inputStream must not be null!");
        Assert.notBlank(resourceName, "resourceName must not be blank!");
        // resource suffix check TODO

        // handle objectName
        objectName = handleObjectName(objectName);

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        if (requireFormat) {
            ObjectMetadata meta = new ObjectMetadata();
            // set contentLength
            meta.setContentLength(resourceSize);
            objectRequest.setMetadata(meta);
        }

        if (isShowProgressBar) {
            objectRequest.withProgressListener(new OssProgressListener());
        }

        OSS ossClient = generateOssClient();
        ossClient.putObject(objectRequest);
        shutdownOssClient(ossClient);

        OssResourceDTO ossResourceDTO = new OssResourceDTO();
        ossResourceDTO.setResourceName(resourceName);
        ossResourceDTO.setResourceSize(resourceSize);
        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }

    /**
     * multipart upload resource to oss
     * for larger resource
     *
     * @param bytes        byte array of resource
     * @param resourceSize name of resource
     * @param objectName   objectName
     * @param segmentSize  segmentSize(min value 100mb)
     * @throws JarvisException exception
     */
    private OssResourceDTO multipartUpload(byte[] bytes, long resourceSize, @Nullable String objectName, long segmentSize) throws JarvisException {
        Assert.notNull(bytes, "bytes must not be null!");
        Assert.isTrue(MIN_SEGMENT_SIZE < segmentSize, String.format("segmentSize[%d] must be greater than min segmentSize", segmentSize));

        objectName = handleObjectName(objectName);

        OSS ossClient = generateOssClient();
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, objectName);
//        initiateMultipartUploadRequest.setObjectMetadata();

        InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(initiateMultipartUploadRequest);
        // multipart upload event id
        String uploadId = initiateMultipartUploadResult.getUploadId();
        List<PartETag> tags = new ArrayList<>();

        int partCount = (int) Math.ceil((double) resourceSize / segmentSize);

//        CountDownLatch countDownLatch = new CountDownLatch(partCount);

        for (int i = 0; i < partCount; i++) {
            long startPos = i * segmentSize;
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                byteArrayInputStream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(byteArrayInputStream);
                uploadPartRequest.setPartSize((i + 1 == partCount) ? (resourceSize - startPos) : segmentSize);
                uploadPartRequest.setPartNumber(i + 1);

                // listener
                uploadPartRequest.withProgressListener(new OssProgressListener());
                // upload
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                tags.add(uploadPartResult.getPartETag());

                // use thread pool
//                threadPoolTaskExecutor.execute((Runnable) () -> {
//                    UploadPartResult uploadPartResult1 = ossClient.uploadPart(uploadPartRequest);
//                    tags.add(uploadPartResult1.getPartETag());
//                    countDownLatch.countDown();
//                });
            } catch (IOException e) {
                throw new JarvisException("multipart upload to oss fail", e);
            }
        }

        // countDownLatch.await();
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, tags);
        completeMultipartUploadRequest.withProgressListener(new OssProgressListener());
        // complete
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        shutdownOssClient(ossClient);

        OssResourceDTO ossResourceDTO = new OssResourceDTO();
        ossResourceDTO.setResourceSize(resourceSize);
        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }

    /**
     * download resource from oss
     *
     * @param objectName  objectName
     * @param securityKey securityKey of resource
     * @param isEncrypt   Whether to encrypt
     * @return byte array of resource
     */
    public byte[] download(String objectName, @Nullable String securityKey, boolean isEncrypt) throws IOException {
        if (isEncrypt && (null == securityKey || securityKey.length() == 0)) {
            throw new JarvisException("securityKey is illegal");
        }
        OSS ossClient = generateOssClient();
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, objectName)
                .withProgressListener(new OssProgressListener()));
        try (InputStream inputStream = ossObject.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } finally {
            shutdownOssClient(ossClient);
        }
    }

    /**
     * download resource from oss by url
     *
     * @param ossUrl         oss resource url
     * @param requestHeaders request headers
     * @return byte array of resource
     */
    public byte[] downloadByOssUrl(String ossUrl, @Nullable Map<String, String> requestHeaders) throws IOException {
        URL url = new URL(ossUrl);
        OSS ossClient = generateOssClient();
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(url, requestHeaders)
                .withProgressListener(new OssProgressListener()));
        try (InputStream inputStream = ossObject.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } finally {
            shutdownOssClient(ossClient);
        }
    }

    /**
     * generate oss temporary url
     *
     * @param objectName objectName
     * @param expireTime url expire time
     * @return url
     */
    public URL generateOssUrl(String objectName, long expireTime) {
        OSS ossClient = generateOssClient();
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        shutdownOssClient(ossClient);
        return url;
    }

    /**
     * delete from oss
     *
     * @param objectName objectName
     */
    public void delete(String objectName) {
        OSS ossClient = generateOssClient();
        ossClient.deleteObject(new GenericRequest(bucketName, objectName)
                .withProgressListener(new OssProgressListener()));
        shutdownOssClient(ossClient);
    }

    /**
     * generate ossClient
     *
     * @return ossClient
     */
    protected OSS generateOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * shutdown ossClient
     *
     * @param ossClient ossClient
     */
    protected void shutdownOssClient(OSS ossClient) {
        ossClient.shutdown();
    }

    /**
     * handle objectName
     *
     * @param objectName objectName of oss
     * @return objectName
     */
    private String handleObjectName(String objectName) {
        if (StringUtils.isBlank(objectName)) {
            objectName = new StringJoiner(DelimiterType.slash.getValue()).add(DEFAULT_FOLDER)
                    .add(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now())).add(UUID.randomUUID().toString()).toString();
        }
        return objectName;
    }
}
