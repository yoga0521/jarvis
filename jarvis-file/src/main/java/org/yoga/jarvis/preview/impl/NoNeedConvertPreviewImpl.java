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
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @Description: no need convert preview
 * ep: jpg,jpeg,png,pdf
 * @Author: yoga
 * @Date: 2023/8/9 10:59
 */
@Slf4j
public class NoNeedConvertPreviewImpl extends AbstractPreview {

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID() + DelimiterType.point.getValue()
                + FileUtils.getFileSuffix(srcFile.getName()));
        try {
            Files.copy(srcFile.toPath(), previewTmpFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new JarvisException(e);
        }
        return previewTmpFile;
    }
}
