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

import org.yoga.jarvis.bean.OssResourceDTO;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: oss handler interface
 * @Author: yoga
 * @Date: 2022/5/17 18:08
 */
public interface OssHandler {

    OssResourceDTO upload(InputStream inputStream, String resourceName, long resourceSize, String objectName,
                          boolean requireFormat, boolean isShowProgressBar);

    /**
     * download resource from oss
     *
     * @param objectName objectName
     * @return byte array of resource
     */
    byte[] download(String objectName) throws IOException;

    /**
     * generate oss temporary url
     *
     * @param objectName objectName
     * @param expireTime url expire time
     * @return url str
     */
    String generateOssUrl(String objectName, long expireTime);

    /**
     * delete from oss
     *
     * @param objectName objectName
     */
    void delete(String objectName);

}
