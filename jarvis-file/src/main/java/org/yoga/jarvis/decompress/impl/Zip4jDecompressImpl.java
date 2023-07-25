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

package org.yoga.jarvis.decompress.impl;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.Charsets;
import org.yoga.jarvis.decompress.AbstractDecompress;
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;

/**
 * @Description: zip4j decompress
 * @Author: yoga
 * @Date: 2023/6/28 13:30
 */
@Slf4j
public class Zip4jDecompressImpl extends AbstractDecompress {

    @Override
    protected void decompressActual(@NonNull File srcFile, @NonNull File destDir) {
        for (int i = 0; i < Charsets.getChineseCharsets().size(); i++) {
            try (ZipFile zipFile = new ZipFile(srcFile)) {
                zipFile.setCharset(Charsets.getChineseCharsets().get(i).getCharset());
                if (zipFile.isEncrypted()) {
                    throw new JarvisException("zip file is encrypted!");
//                zipFile.setPassword(password.toCharArray());
                }
                zipFile.extractAll(destDir.getPath());
                break;
            } catch (IOException e) {
                log.error("failed to decompress with {} encoding!", Charsets.getChineseCharsets().get(i).getCharsetName(), e);
                if (i == Charsets.getChineseCharsets().size() - 1) {
                    throw new JarvisException("decompress fail!");
                }
            }
        }
    }
}
