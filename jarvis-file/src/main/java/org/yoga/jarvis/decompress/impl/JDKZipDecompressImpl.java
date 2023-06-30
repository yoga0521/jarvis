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
import org.springframework.lang.NonNull;
import org.yoga.jarvis.decompress.AbstractDecompress;
import org.yoga.jarvis.exception.JarvisException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description: jdk zip decompress
 * @Author: yoga
 * @Date: 2023/6/28 13:46
 */
@Slf4j
public class JDKZipDecompressImpl extends AbstractDecompress {

    @Override
    protected void decompressActual(@NonNull File srcFile, @NonNull File destDir) {
        try (FileInputStream fis = new FileInputStream(srcFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File target = new File(destDir, entry.getName());
                byte[] buffer;
                try (FileOutputStream fos = new FileOutputStream(target);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    int read;
                    buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
            }
        } catch (IOException e) {
            log.error("decompress fail!", e);
            throw new JarvisException("decompress fail!");
        }
    }
}
