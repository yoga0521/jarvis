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
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;

/**
 * @Description: other preview
 * @Author: yoga
 * @Date: 2023/7/10 17:23
 */
@Slf4j
public class OtherPreviewImpl extends AbstractPreview {

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        try {
            String fileType = new Tika().detect(srcFile);
            log.error("It don't support the preview of the {} file type!", fileType);
            throw new JarvisException("It don't support the preview of the " + fileType + " file type");
        } catch (IOException e) {
            log.error("Other preview impl class fail to get the file type, use suffix!", e);
            throw new JarvisException("It don't support the decompression of the " + FilenameUtils.getExtension(srcFile.getName()) + " file type");
        }
    }
}
