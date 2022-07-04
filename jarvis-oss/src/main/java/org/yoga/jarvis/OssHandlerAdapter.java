/*
 *  Copyright 2022 yoga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.yoga.jarvis;

import org.springframework.lang.Nullable;
import org.yoga.jarvis.adapter.Adapter;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.exception.OssException;

import java.io.InputStream;

/**
 * @Description: oss handler adapter
 * @Author: yoga
 * @Date: 2022/7/4 17:38
 */
public class OssHandlerAdapter implements Adapter, OssHandler {

    /**
     * Basic upload maximum resource size
     * 5GB
     */
    private final long MAX_RESOURCE_SIZE = 5 * 1024 * 1024L;

    private DefaultOssHandler defaultOssHandler;

    private MultipartOssHandler multipartOssHandler;

    public OssHandlerAdapter(DefaultOssHandler defaultOssHandler, MultipartOssHandler multipartOssHandler) {
        this.defaultOssHandler = defaultOssHandler;
        this.multipartOssHandler = multipartOssHandler;
    }

    @Override
    public OssResourceDTO upload(InputStream input, String resourceName, long resourceSize) throws OssException {
        if (MAX_RESOURCE_SIZE > resourceSize) {
            return defaultOssHandler.upload(input, resourceName, resourceSize);
        }
        return multipartOssHandler.upload(input, resourceName, resourceSize);
    }

    @Override
    public OssResourceDTO upload(InputStream input, String resourceName, long resourceSize, @Nullable String objectName, boolean requireFormat, boolean isShowProgressBar) throws OssException {
        if (MAX_RESOURCE_SIZE > resourceSize) {
            return defaultOssHandler.upload(input, resourceName, resourceSize, objectName, requireFormat, isShowProgressBar);
        }
        return multipartOssHandler.upload(input, resourceName, resourceSize, objectName, requireFormat, isShowProgressBar);
    }

    @Override
    public byte[] download(String objectName) throws OssException {
        return defaultOssHandler.download(objectName);
    }

    @Override
    public String generateOssUrl(String objectName, long expiration) throws OssException {
        return defaultOssHandler.generateOssUrl(objectName, expiration);
    }

    @Override
    public void delete(String objectName) throws OssException {
        defaultOssHandler.delete(objectName);
    }
}
