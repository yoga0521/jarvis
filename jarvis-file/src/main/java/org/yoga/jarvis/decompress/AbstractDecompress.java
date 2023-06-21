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

package org.yoga.jarvis.decompress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.util.Assert;

import java.io.File;

/**
 * @Description: abstract decompress
 * @Author: yoga
 * @Date: 2023/6/21 16:07
 */
@Slf4j
public abstract class AbstractDecompress implements Decompress {

    @Override
    public void decompress(@NonNull File srcFile, @NonNull File destDir) {
        Assert.notNull(srcFile, "source file is null!");
        Assert.isTrue(srcFile.exists(), "source file not exist!");
        Assert.isTrue(srcFile.canRead(), "source file has no read permission!");
        Assert.isTrue(srcFile.isFile(), "source file is not a file!");
        Assert.notNull(destDir, "dest dir is null!");
        Assert.isTrue(destDir.exists(), "dest dir not exist!");
        Assert.isTrue(destDir.canExecute(), "dest dir has no exec permission!");
        Assert.isTrue(destDir.isDirectory(), "dest dir is not a directory!");
        long startTime = System.currentTimeMillis();
        decompressActual(srcFile, destDir);
        log.info("decompress success，compressed file size：{}B，decompress file size：{}B，cost：{}ms",
                FileUtils.sizeOfAsBigInteger(srcFile), FileUtils.sizeOfAsBigInteger(destDir), System.currentTimeMillis() - startTime);
    }

    /**
     * decompress actual function
     *
     * @param srcFile compress source file
     * @param destDir decompressed file dir
     */
    protected abstract void decompressActual(@NonNull File srcFile, @NonNull File destDir);
}
