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
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.yoga.jarvis.beans.OssProperties;
import org.yoga.jarvis.beans.OssResourceDTO;
import org.yoga.jarvis.constants.DelimiterType;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.utils.Assert;
import org.yoga.jarvis.utils.IOUtils;
import org.yoga.jarvis.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: Abstract oss handler
 * @Author: yoga
 * @Date: 2022/5/18 9:57
 */
public abstract class AbstractOssHandler implements OssHandler {

    protected final Executor executor = new ThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors(),
            4 * Runtime.getRuntime().availableProcessors(),
            5000L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(1024),
            r -> {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                final AtomicLong count = new AtomicLong(0);
                thread.setName(String.format("oss-pool-%d", count.getAndIncrement()));
                return thread;
            });

    /**
     * Default folder
     */
    private static final String DEFAULT_FOLDER = "default";

    /**
     * Oss internal endpoint suffix
     */
    private static final String OSS_INTERNAL_ENDPOINT_SUFFIX = "-internal";

    protected OssProperties ossProperties;

    protected AbstractOssHandler(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
        // check param TODO
    }

    @Override
    public OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, String objectName, boolean requireFormat, boolean isShowProgressBar) {
        return null;
    }

    @Override
    public byte[] download(String objectName) throws IOException {
        Assert.notBlank(objectName, "objectName must not be blank!");
        OSS ossClient = generateOssClient();
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(ossProperties.getBucketName(), objectName)
                .withProgressListener(new OssProgressListener()));
        try (InputStream inputStream = ossObject.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } finally {
            shutdownOssClient(ossClient);
        }
    }

    @Override
    public String generateOssUrl(String objectName, long expireTime) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        Assert.isTrue(expireTime > 0, "expireTime must greater than 0!");

        OSS ossClient = generateOssClient();
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        URL url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), objectName, expiration);
        shutdownOssClient(ossClient);
        return url.toString();
    }

    @Override
    public void delete(String objectName) {
        Assert.notBlank(objectName, "objectName must not be blank!");

        OSS ossClient = generateOssClient();
        ossClient.deleteObject(new GenericRequest(ossProperties.getBucketName(), objectName)
                .withProgressListener(new OssProgressListener()));
        shutdownOssClient(ossClient);
    }

    /**
     * generate ossClient
     *
     * @return ossClient
     */
    protected OSS generateOssClient() {
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
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
    protected String handleObjectName(String objectName) {
        if (StringUtils.isBlank(objectName)) {
            objectName = new StringJoiner(DelimiterType.slash.getValue()).add(DEFAULT_FOLDER)
                    .add(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now())).add(UUID.randomUUID().toString()).toString();
        }
        return objectName;
    }
}
