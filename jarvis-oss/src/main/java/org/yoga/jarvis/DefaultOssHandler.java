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
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.yoga.jarvis.bean.OssConfigs;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.constant.OssOperateType;
import org.yoga.jarvis.listener.OssProgressListener;
import org.yoga.jarvis.util.Assert;

import java.io.InputStream;

/**
 * @Description: Oss operate handler
 * @Author: yoga
 * @Date: 2022/5/13 14:29
 */
public class DefaultOssHandler extends AbstractOssHandler {

    /**
     * Basic upload maximum resource size
     * 5GB
     */
    private final long MAX_RESOURCE_SIZE = 5 * 1024 * 1024L;

    public DefaultOssHandler(OssConfigs ossConfigs) {
        super(ossConfigs);
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
     * @return oss result {@link org.yoga.jarvis.bean.OssResourceDTO}
     */
    @Override
    protected OssResourceDTO uploadActual(InputStream inputStream, String resourceName, long resourceSize, String objectName,
                                       boolean requireFormat, boolean isShowProgressBar) throws IllegalArgumentException {
        Assert.isTrue(resourceSize < MAX_RESOURCE_SIZE, "the resource is too large, please use multipart upload!");
        // resource suffix check TODO

        PutObjectRequest objectRequest = new PutObjectRequest(ossConfigs.getBucketName(), objectName, inputStream);
        if (requireFormat) {
            ObjectMetadata meta = new ObjectMetadata();
            // set contentLength
            meta.setContentLength(resourceSize);
            objectRequest.setMetadata(meta);
        }

        if (isShowProgressBar) {
            objectRequest.withProgressListener(new OssProgressListener(OssOperateType.upload));
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

}
