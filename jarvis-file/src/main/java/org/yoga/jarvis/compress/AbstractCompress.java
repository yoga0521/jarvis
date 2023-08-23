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

package org.yoga.jarvis.compress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.util.Assert;

import java.io.File;

/**
 * @Description: Abstract Compress
 * @Author: yoga
 * @Date: 2023/8/22 10:02
 */
@Slf4j
public abstract class AbstractCompress implements Compress {

    @Override
    public void compress(@NonNull File srcFileDir, @NonNull File destDir) {
        Assert.notNull(srcFileDir, "source file dir is null!");
        Assert.isTrue(srcFileDir.exists(), "source file dir not exist!");
        Assert.isTrue(srcFileDir.canRead(), "source file dir has no read permission!");
        Assert.notNull(destDir, "dest dir is null!");
        Assert.isTrue(destDir.exists(), "dest dir not exist!");
        Assert.isTrue(destDir.canExecute(), "dest dir has no exec permission!");
        Assert.isTrue(destDir.isDirectory(), "dest dir is not a directory!");
        long startTime = System.currentTimeMillis();
        compressActual(srcFileDir, destDir);
        log.info("compress success，need compress file size：{}B，compressed file size：{}B，cost：{}ms",
                FileUtils.sizeOfAsBigInteger(srcFileDir), FileUtils.sizeOfAsBigInteger(destDir), System.currentTimeMillis() - startTime);

    }

    /**
     * compress actual function
     *
     * @param srcFileDir need compress source file dir
     * @param destDir    compressed file dir
     */
    protected abstract void compressActual(@NonNull File srcFileDir, @NonNull File destDir);
}
