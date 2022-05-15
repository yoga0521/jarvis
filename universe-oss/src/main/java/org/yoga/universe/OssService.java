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

package org.yoga.universe;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import org.springframework.lang.Nullable;
import org.yoga.universe.beans.OssResourceDTO;
import org.yoga.universe.constants.DelimiterType;
import org.yoga.universe.exception.UniverseException;
import org.yoga.universe.listener.OssProgressListener;
import org.yoga.universe.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: TODO
 * @Author: yoga
 * @Date: 2022/5/13 14:29
 */
public class OssService {

    /**
     * default folder
     */
    private static final String DEFAULT_FOLDER = "default";

    /**
     * minimum size of segment(100 * 1024kb)
     */
    private static final long MIN_SEGMENT_SIZE = 102_400;

    /**
     * endpoint of oss
     */
    private String endpoint;

    /**
     * external_endpoint
     */
    private String externalEndpoint;

    /**
     * bucket name of oss
     */
    private String bucketName;

    /**
     * access key of oss
     */
    private String accessKeyId;

    /**
     * secret key of oss
     */
    private String secretAccessKey;

    /**
     * Maximum size of resource
     */
    private long resourceMaxSize;


    public OssService(String endpoint, String externalEndpoint, String bucketName, String accessKeyId, String secretAccessKey, long resourceMaxSize) {
        this.endpoint = endpoint;
        this.externalEndpoint = externalEndpoint;
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.resourceMaxSize = resourceMaxSize;
        // oss configuration parameter is illegal
        // check param TODO
    }

    /**
     * upload resource to oss
     *
     * @param inputStream   resource
     * @param resourceName  name of resource
     * @param resourceSize  size of resource
     * @param folder        oss folder, Use the default folder {@value DEFAULT_FOLDER} when the folder is blank
     * @param objectName    objectName
     * @param requireFormat Whether to require resource format
     * @param isEncrypt     Whether to encrypt
     * @param isShowProgressBar Whether to show progress bar
     * @return oss result {@link org.yoga.universe.beans.OssResourceDTO}
     */
    private OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, @Nullable String folder,
                                  @Nullable String objectName, boolean requireFormat, boolean isEncrypt, boolean isShowProgressBar) {
        // not null check inputStream,resourceName TODO
        // resource size check TODO
        // resource suffix check TODO
        OssResourceDTO ossResourceDTO = new OssResourceDTO();
        ossResourceDTO.setResourceName(resourceName);
        ossResourceDTO.setResourceSize(resourceSize);


        ObjectMetadata meta = new ObjectMetadata();
        // handle objectName
        objectName = handleObjectName(objectName, folder);
        // If there is a requirement for the resource format, set contentType TODO

        // set contentLength
        meta.setContentLength(resourceSize);

        OSS ossClient = generateOssClient();

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, objectName, inputStream, meta);
        if (isShowProgressBar) {
            objectRequest.withProgressListener(new OssProgressListener());
        }
        ossClient.putObject(objectRequest);
        shutdownOssClient(ossClient);

        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }

    /**
     * multipart upload resource to oss
     * for larger resource
     *
     * @param bytes        byte array of resource
     * @param resourceSize name of resource
     * @param folder       oss folder, Use the default folder {@value DEFAULT_FOLDER} when the folder is blank
     * @param objectName   objectName
     * @param partSize     partSize(min value 100mb)
     * @throws UniverseException exception
     */
    private OssResourceDTO multipartUpload(byte[] bytes, long resourceSize, @Nullable String folder, @Nullable String objectName,
                                           long partSize) throws UniverseException {
//        Assert.notNull(bytes, "bytes is null");
//        Assert.isTrue(MIN_PART_SIZE < partSize, String.format("partSize[%d] smaller than min partSize", partSize));

        OssResourceDTO ossResourceDTO = new OssResourceDTO();
        ossResourceDTO.setResourceSize(resourceSize);

        objectName = handleObjectName(objectName, folder);
        OSS ossClient = generateOssClient();
        InitiateMultipartUploadResult initiateMultipartUploadResult =
                ossClient.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, objectName));
        String uploadId = initiateMultipartUploadResult.getUploadId();
        List<PartETag> tags = new ArrayList<>();
        int partCount = (int) Math.ceil((double) resourceSize / partSize);

        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                byteArrayInputStream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(byteArrayInputStream);
                uploadPartRequest.setPartSize((i + 1 == partCount) ? (resourceSize - startPos) : partSize);
                uploadPartRequest.setPartNumber(i + 1);
                // upload
                UploadPartResult uploadPartResult =
                        ossClient.uploadPart(uploadPartRequest.withProgressListener(new OssProgressListener()));
                tags.add(uploadPartResult.getPartETag());
            } catch (IOException e) {
                throw new UniverseException("multipart upload to oss fail", e);
            }
        }

        tags.sort(Comparator.comparingInt(PartETag::getPartNumber));

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, tags);
        // complete
        ossClient.completeMultipartUpload(completeMultipartUploadRequest.withProgressListener(new OssProgressListener()));
        shutdownOssClient(ossClient);
        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }

    /**
     * download file from oss
     *
     * @param objectName  objectName
     * @param securityKey securityKey of resource
     * @param isEncrypt   Whether to encrypt
     * @return byte array of resource
     */
    public byte[] download(String objectName, @Nullable String securityKey, boolean isEncrypt) throws IOException {
        if (isEncrypt && (null == securityKey || securityKey.length() == 0)) {
            throw new UniverseException("securityKey is illegal");
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
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
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
     * @param folder     folder
     * @return objectName
     */
    private String handleObjectName(String objectName, String folder) {
        // remove delimiter TODO
        if (objectName == null || "".equals(objectName)) {
            objectName = new StringJoiner(DelimiterType.slash.getValue()).add(DEFAULT_FOLDER)
                    .add(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now())).add(UUID.randomUUID().toString()).toString();
        }
        return objectName;
    }
}
