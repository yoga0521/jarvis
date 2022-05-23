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
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.springframework.lang.Nullable;
import org.yoga.jarvis.bean.OssProperties;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.constant.OssOperateType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: Oss multipart handler
 * @Author: yoga
 * @Date: 2022/5/17 18:09
 */
public class MultipartOssHandler extends AbstractOssHandler {

    /**
     * Minimum size of segment(100 * 1024kb)
     */
    private static final long MIN_SEGMENT_SIZE = 102_400;

    /**
     * Default size of segment(100 * 1024kb)
     */
    private static final long DEFAULT_SEGMENT_SIZE = 102_400;

    /**
     * Multipart upload maximum resource size
     * 48.8TB
     */
    private final double MAX_RESOURCE_SIZE = 48.8 * 1024 * 1024 * 1024d;

    protected MultipartOssHandler(OssProperties ossProperties) {
        super(ossProperties);
    }

    /**
     * multipart upload resource to oss
     * for larger resource
     *
     * @param input             resource
     * @param resourceName      name of resource
     * @param resourceSize      size of resource
     * @param objectName        objectName
     * @param requireFormat     Whether to require resource format
     * @param isShowProgressBar Whether to show progress bar
     * @throws JarvisException exception
     */
    @Override
    protected OssResourceDTO uploadActual(InputStream input, String resourceName, long resourceSize, @Nullable String objectName,
                                       boolean requireFormat, boolean isShowProgressBar) throws JarvisException {
        Assert.isTrue(resourceSize < MAX_RESOURCE_SIZE, "the resource is too large, not support!");

        OSS ossClient = generateOssClient();
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(ossProperties.getBucketName(), objectName);
//        initiateMultipartUploadRequest.setObjectMetadata();

        InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(initiateMultipartUploadRequest);
        // multipart upload event id
        String uploadId = initiateMultipartUploadResult.getUploadId();
        List<PartETag> tags = new ArrayList<>();

        int partCount = (int) Math.ceil((double) resourceSize / MIN_SEGMENT_SIZE);

        CountDownLatch countDownLatch = new CountDownLatch(partCount);

        for (int i = 0; i < partCount; i++) {
            long startPos = i * MIN_SEGMENT_SIZE;
            try {
                input.skip(startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(ossProperties.getBucketName());
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(input);
                uploadPartRequest.setPartSize((i + 1 == partCount) ? (resourceSize - startPos) : MIN_SEGMENT_SIZE);
                uploadPartRequest.setPartNumber(i + 1);

                // listener
                uploadPartRequest.withProgressListener(new OssProgressListener(OssOperateType.upload));
                // upload
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                tags.add(uploadPartResult.getPartETag());

                // use thread pool
                OSS_THREAD_POOL.execute(() -> {
                    UploadPartResult uploadPartResult1 = ossClient.uploadPart(uploadPartRequest);
                    tags.add(uploadPartResult1.getPartETag());
                    countDownLatch.countDown();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(ossProperties.getBucketName(), objectName, uploadId, tags);
        completeMultipartUploadRequest.withProgressListener(new OssProgressListener(OssOperateType.upload));
        // complete
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        shutdownOssClient(ossClient);

        OssResourceDTO ossResourceDTO = new OssResourceDTO();
        ossResourceDTO.setResourceSize(resourceSize);
        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }
}
