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

import org.springframework.lang.NonNull;

import java.io.File;

/**
 * @Description: decompress
 * @Author: yoga
 * @Date: 2023/6/21 13:10
 */
public interface Decompress {

    /**
     * decompress
     *
     * @param srcFile compress source file
     * @param destDir decompressed file dir
     */
    void decompress(@NonNull File srcFile, @NonNull File destDir);
}
