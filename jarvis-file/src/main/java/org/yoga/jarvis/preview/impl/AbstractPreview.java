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
import org.springframework.lang.NonNull;
import org.yoga.jarvis.preview.Preview;
import org.yoga.jarvis.util.Assert;

import java.io.File;

/**
 * @Description: abstract preview
 * @Author: yoga
 * @Date: 2023/7/5 16:02
 */
@Slf4j
public abstract class AbstractPreview implements Preview {

    @Override
    public void preview(@NonNull File srcFile, @NonNull File destDir) {
        verifyFile(srcFile, "source file");
        Assert.isTrue(srcFile.canRead(), "source file has no read permission!");
        verifyDir(destDir, "dest dir");
        Assert.isTrue(destDir.canExecute(), "dest dir has no exec permission!");
        long startTime = System.currentTimeMillis();
        previewActual(srcFile, destDir);
        log.info("preview success，need preview file size：{}B，previewed file size：{}B，cost：{}ms",
                FileUtils.sizeOfAsBigInteger(srcFile), FileUtils.sizeOfAsBigInteger(destDir), System.currentTimeMillis() - startTime);
    }

    /**
     * preview actual function
     *
     * @param srcFile need preview source file
     * @param destDir previewed file dir
     */
    protected abstract void previewActual(@NonNull File srcFile, @NonNull File destDir);

}
