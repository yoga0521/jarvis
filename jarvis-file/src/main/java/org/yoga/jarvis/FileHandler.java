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

package org.yoga.jarvis;

import org.springframework.lang.NonNull;
import org.yoga.jarvis.util.Assert;

import java.io.File;

/**
 * @Description: file handler
 * @Author: yoga
 * @Date: 2023/7/5 16:06
 */
public interface FileHandler {

    /**
     * verify file
     *
     * @param file     file
     * @param fileName file name
     */
    default void verifyFile(@NonNull File file, @NonNull String fileName) {
        Assert.notNull(file, fileName + " is null!");
        Assert.isTrue(file.exists(), fileName + " not exist!");
        Assert.isTrue(file.canRead(), fileName + " has no read permission!");
        Assert.isTrue(file.isFile(), fileName + " is not a file!");
    }

    /**
     * verify directory
     *
     * @param dir     directory
     * @param dirName directory name
     */
    default void verifyDir(@NonNull File dir, @NonNull String dirName) {
        Assert.notNull(dir, dirName + " is null!");
        Assert.isTrue(dir.exists(), dirName + " not exist!");
        Assert.isTrue(dir.canExecute(), dirName + " has no exec permission!");
        Assert.isTrue(dir.isDirectory(), dirName + " is not a directory!");
    }
}
