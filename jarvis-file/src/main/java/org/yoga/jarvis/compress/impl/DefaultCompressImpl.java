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

package org.yoga.jarvis.compress.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.compress.AbstractCompress;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: default compress
 * @Author: yoga
 * @Date: 2023/8/23 09:39
 */
@Slf4j
public class DefaultCompressImpl extends AbstractCompress {

    @Override
    protected void compressActual(@NonNull File srcFile, @NonNull File destDir) {
        File zipFile = new File(destDir.getPath() + File.separator + srcFile.getName() + DelimiterType.point + "zip");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            // compress level, -1(default) 1(fast but inefficient) 9(fast and efficient)
            zos.setLevel(Deflater.BEST_COMPRESSION);
            // set the comment for zip file
            // zos.setComment("zip文件说明");
            // handle folder
            if (srcFile.isDirectory() && ArrayUtils.isNotEmpty(srcFile.listFiles())) {
                for (File file : srcFile.listFiles()) {
                    addZipFile(file, zos);
                }
            } else {
                addZipFile(srcFile, zos);
            }
        } catch (IOException e) {
            throw new JarvisException(e);
        }
    }

    /**
     * add zip file
     *
     * @param file file
     * @param zos  ZipOutputStream
     * @throws IOException IOException
     */
    private void addZipFile(File file, ZipOutputStream zos) throws IOException {
        if (!file.exists()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);) {
            // create a compress object and set properties
            ZipEntry entry = new ZipEntry(file.getName());
            // set the uncompressed data size
            entry.setSize(file.length());
            // set the compressed size
            entry.setCompressedSize(file.length());
            // set extra data is empty
            entry.setExtra(new byte[]{});
            // set the comment for Zip Entry
//            entry.setComment("file comment");
            // set file creation time
            FileTime fileTime = FileTime.from(Instant.now());
            entry.setCreationTime(fileTime);
            // set the last access time of the file
            entry.setLastAccessTime(fileTime);
            // set the last modification time of the file
            entry.setLastModifiedTime(fileTime);
            zos.putNextEntry(entry);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
        }
    }
}
