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
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.lang.Nullable;
import org.yoga.jarvis.beans.OssProperties;
import org.yoga.jarvis.beans.OssResourceDTO;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.utils.Assert;
import org.yoga.jarvis.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @Description: Oss operate handler
 * @Author: yoga
 * @Date: 2022/5/13 14:29
 */
public class DefaultOssHandler extends AbstractOssHandler {

    public DefaultOssHandler(OssProperties ossProperties) {
        super(ossProperties);
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
    @Override
    public OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, @Nullable String objectName,
                                 boolean requireFormat, boolean isShowProgressBar) throws IllegalArgumentException {
        Assert.isTrue(resourceSize < ossProperties.getCommonUploadMaxSize(), "the resource is too large, please use multipart upload!");
        Assert.notNull(inputStream, "inputStream must not be null!");
        Assert.notBlank(resourceName, "resourceName must not be blank!");
        // resource suffix check TODO

        // handle objectName
        objectName = handleObjectName(objectName);

        PutObjectRequest objectRequest = new PutObjectRequest(ossProperties.getBucketName(), objectName, inputStream);
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

}
