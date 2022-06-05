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

package org.yoga.jarvis.spring.boot.autoconfigure.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

/**
 * @Description: Oss Properties
 * @Author: yoga
 * @Date: 2022/5/25 13:22
 */
@ConfigurationProperties(prefix = "jarvis.oss")
public class OssProperties {

    /**
     * Whether to enable oss auto configuration
     */
    private boolean enabled = true;

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

    public boolean isEnabled() {
        return enabled;
    }

    public OssProperties setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public OssProperties setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public OssProperties setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public OssProperties setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public OssProperties setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public OssProperties setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OssProperties.class.getSimpleName() + "[", "]")
                .add("enabled=" + enabled)
                .add("endpoint='" + endpoint + "'")
                .add("bucketName='" + bucketName + "'")
                .add("accessKeyId='" + accessKeyId + "'")
                .add("accessKeySecret='" + accessKeySecret + "'")
                .add("partSize=" + partSize)
                .toString();
    }
}
