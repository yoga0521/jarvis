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

import org.springframework.lang.Nullable;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.exception.OssException;

import java.io.InputStream;

/**
 * @Description: oss handler interface
 * @Author: yoga
 * @Date: 2022/5/17 18:08
 */
public interface OssHandler {

    /**
     * upload the resource to oss
     * with default objectName, requireFormat
     * and don't show progress bar
     *
     * @param input        the inputStream of resource
     * @param resourceName the name of resource
     *                     use UTF-8 encoding, the length must be between 1 and 255 characters
     * @param resourceSize the size of resource
     * @return oss resource {@link org.yoga.jarvis.bean.OssResourceDTO}
     * @throws OssException exception during upload
     */
    OssResourceDTO upload(InputStream input, String resourceName, long resourceSize) throws OssException;

    /**
     * upload the resource to oss
     *
     * @param input             the inputStream of resource
     * @param resourceName      the name of resource
     *                          use UTF-8 encoding, the length must be between 1 and 255 characters
     * @param resourceSize      the size of resource
     * @param objectName        the name of oss object
     *                          use UTF-8 encoding, the length must be between 1 and 1023 characters, can't start with a slash (/) or a backslash (\)
     * @param requireFormat     whether format require
     * @param isShowProgressBar whether show progress bar
     * @return oss resource {@link org.yoga.jarvis.bean.OssResourceDTO}
     * @throws OssException exception during upload
     */
    OssResourceDTO upload(InputStream input, String resourceName, long resourceSize, @Nullable String objectName,
                          boolean requireFormat, boolean isShowProgressBar) throws OssException;

    /**
     * download resource from oss
     *
     * @param objectName the name of oss object
     *                   use UTF-8 encoding, the length must be between 1 and 1023 characters, can't start with a slash (/) or a backslash (\)
     * @return the byte array of resource
     * @throws OssException exception during download
     */
    byte[] download(String objectName) throws OssException;

    /**
     * generate oss temporary url
     *
     * @param objectName the name of oss object
     *                   use UTF-8 encoding, the length must be between 1 and 1023 characters, can't start with a slash (/) or a backslash (\)
     * @param expiration the expiration of temporary url
     * @return the str of temporary url
     * @throws OssException exception during generate
     */
    String generateOssUrl(String objectName, long expiration) throws OssException;

    /**
     * delete from oss
     *
     * @param objectName the name of oss object
     *                   use UTF-8 encoding, the length must be between 1 and 1023 characters, can't start with a slash (/) or a backslash (\)
     * @throws OssException exception during generate
     */
    void delete(String objectName) throws OssException;

}
