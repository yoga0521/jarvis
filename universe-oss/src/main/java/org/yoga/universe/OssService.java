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

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.lang.Nullable;
import org.yoga.universe.beans.OssResourceDTO;
import org.yoga.universe.constants.DelimiterType;
import org.yoga.universe.listener.OssProgressListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

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
     * 授权密钥Id
     */
    private String accessKeyId;

    /**
     * 授权密钥Secret
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
     * @param inputStream   文件流
     * @param resourceName  name of resource
     * @param resourceSize  size of resource
     * @param folder        oss folder, Use the default folder {@value DEFAULT_FOLDER} when the folder is blank
     * @param objectName    objectName
     * @param requireFormat Whether to require resource format
     * @param isEncrypt     Whether to encrypt
     * @return oss result {@link org.yoga.universe.beans.OssResourceDTO}
     */
    private OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, @Nullable String folder,
                                  @Nullable String objectName, boolean requireFormat, boolean isEncrypt) {
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

        OSSClient ossClient = generateOssClient();

        ossClient.putObject(new PutObjectRequest(bucketName, objectName, inputStream, meta)
                .withProgressListener(new OssProgressListener()));
        shutdownOssClient(ossClient);

        ossResourceDTO.setObjectName(objectName);
        return ossResourceDTO;
    }

    /**
     * download resource from oss by url
     *
     * @param ossUrl         oss resource url
     * @param requestHeaders requestH headers
     * @return byte array of resource
     */
    public byte[] downloadByOssUrl(String ossUrl, @Nullable Map<String, String> requestHeaders) throws IOException {
        URL url = new URL(ossUrl);
        OSSClient ossClient = generateOssClient();
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(url, requestHeaders)
                .withProgressListener(new OssProgressListener()));
        try (InputStream inputStream = ossObject.getObjectContent()) {
            // IOUtils.toByteArray(inputStream); TODO
            return null;
        } finally {
            shutdownOssClient(ossClient);
        }
    }

    /**
     * generate ossClient
     *
     * @return ossClient
     */
    protected OSSClient generateOssClient() {
        return new OSSClient(endpoint, new DefaultCredentialProvider(accessKeyId, secretAccessKey), null);
    }

    /**
     * shutdown ossClient
     *
     * @param ossClient ossClient
     */
    protected void shutdownOssClient(OSSClient ossClient) {
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
