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

package org.yoga.jarvis.preview.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @Description: sim text file preview
 * @Author: yoga
 * @Date: 2023/8/1 16:23
 */
@Slf4j
public class SimTextPreviewImpl extends AbstractPreview {

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID() + ".txt");

        try (InputStream is = FileUtils.openInputStream(srcFile);
             AutoDetectReader autoDetectReader = new AutoDetectReader(is)) {
            if (!StandardCharsets.UTF_8.name().equals(autoDetectReader.getCharset().name())) {
                List<String> content = FileUtils.readLines(srcFile, autoDetectReader.getCharset().name());
                FileUtils.writeLines(previewTmpFile, StandardCharsets.UTF_8.name(), content);
            } else {
                FileUtils.copyFile(srcFile, previewTmpFile);
            }
        } catch (IOException | TikaException e) {
            throw new JarvisException(e);
        }
        return previewTmpFile;
    }
}
