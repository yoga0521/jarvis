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

package org.yoga.jarvis.bean;

/**
 * @Description: Oss configs
 * @Author: yoga
 * @Date: 2022/5/18 10:34
 */
public class OssConfigs {
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
     * Part size for multipart upload, default 100 MB
     */
    private long partSize = 102_400;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public long getPartSize() {
        return partSize;
    }

    public OssConfigs setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }
}
