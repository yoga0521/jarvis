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

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.yoga.jarvis.bean.OssConfigs;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.constant.OssOperateType;
import org.yoga.jarvis.exception.OssException;
import org.yoga.jarvis.factory.ThreadPoolFactory;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.IOUtils;
import org.yoga.jarvis.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @Description: Abstract oss handler
 * @Author: yoga
 * @Date: 2022/5/18 9:57
 */
public abstract class AbstractOssHandler implements OssHandler {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractOssHandler.class);

    /**
     * the threadPool for doing asynchronous task
     */
    protected static final ExecutorService OSS_THREAD_POOL = ThreadPoolFactory.generateIOThreadPool("oss-pool-%d");

    /**
     * default folder
     */
    private static final String DEFAULT_FOLDER = "default";

    /**
     * oss internal endpoint suffix
     */
    private static final String OSS_INTERNAL_ENDPOINT_SUFFIX = "-internal";

    /**
     * the number of resources to delete in bulk
     */
    private final int BATCH_DELETE_RESOURCE_NUMBER = 1000;

    protected final OssConfigs ossConfigs;

    protected AbstractOssHandler(OssConfigs ossConfigs) {
        Assert.notNull(ossConfigs, "ossConfigs must not be null!");
        this.ossConfigs = ossConfigs;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            OSS_THREAD_POOL.shutdown();
            logger.warn("thread pool is shutdown");
        }));
    }

    @Override
    public OssResourceDTO upload(InputStream input, String resourceName, long resourceSize) throws OssException {
        return upload(input, resourceName, resourceSize, null, true, false);
    }

    @Override
    public OssResourceDTO upload(InputStream input, String resourceName, long resourceSize, @Nullable String objectName,
                                 boolean requireFormat, boolean isShowProgressBar) throws OssException {
        Assert.notNull(input, "input must not be null!");
        Assert.notBlank(resourceName, "resourceName must not be blank!");

        try {
            return uploadActual(input, resourceName, resourceSize, handleObjectName(objectName), requireFormat, isShowProgressBar);
        } catch (OSSException oe) {
            logger.error("Caught an OSSException, which means your request made it to OSS," +
                    " but was rejected with an error response for some reason.");
            logger.error("Error Code:{}, Request ID:{}, Host ID:{}, Error Message:{}",
                    oe.getErrorCode(), oe.getRequestId(), oe.getHostId(), oe.getErrorMessage());
            throw new OssException(oe.getErrorMessage());
        } catch (ClientException ce) {
            logger.error("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS," +
                    " such as not being able to access the network.");
            logger.error("Error Message:{}", ce.getMessage());
            throw new OssException(ce.getMessage());
        }
    }

    protected abstract OssResourceDTO uploadActual(InputStream input, String resourceName, long resourceSize, String objectName,
                                                   boolean requireFormat, boolean isShowProgressBar);

    @Override
    public byte[] download(String objectName) throws OssException {
        Assert.notBlank(objectName, "objectName must not be blank!");
        OSS ossClient = generateOssClient();
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(ossConfigs.getBucketName(), objectName)
                .withProgressListener(new OssProgressListener(OssOperateType.download)));
        try (InputStream inputStream = ossObject.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            logger.error("Caught an IOException, when download resource.");
            logger.error("Error Message:{}", e.getMessage());
            throw new OssException(e.getMessage());
        } finally {
            shutdownOssClient(ossClient);
        }
    }

    @Override
    public String generateOssUrl(String objectName, long expiration) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        Assert.isTrue(expiration > 0, "expireTime must greater than 0!");

        OSS ossClient = generateOssClient();
        URL url = ossClient.generatePresignedUrl(ossConfigs.getBucketName(), objectName, new Date(System.currentTimeMillis() + expiration));
        shutdownOssClient(ossClient);
        return url.toString();
    }

    @Override
    public void delete(String objectName) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        OSS ossClient = generateOssClient();
        ossClient.deleteObject(new GenericRequest(ossConfigs.getBucketName(), objectName));
        shutdownOssClient(ossClient);
    }

    /**
     * generate ossClient
     *
     * @return ossClient
     */
    protected OSS generateOssClient() {
        return new OSSClientBuilder().build(ossConfigs.getEndpoint(), ossConfigs.getAccessKeyId(), ossConfigs.getAccessKeySecret());
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
     * handle the name of oss object, when the objectName is blank
     * If the objectName is empty, set the default objectName
     *
     * @param objectName the name of oss object
     *                   use UTF-8 encoding, the length must be between 1 and 1023 characters, can't start with a slash (/) or a backslash (\)
     * @return the name of oss object
     */
    protected String handleObjectName(@Nullable String objectName) {
        if (StringUtils.isBlank(objectName)) {
            objectName = new StringJoiner(DelimiterType.slash.getValue()).add(DEFAULT_FOLDER)
                    .add(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now())).add(UUID.randomUUID().toString()).toString();
        }
        return objectName;
    }
}
