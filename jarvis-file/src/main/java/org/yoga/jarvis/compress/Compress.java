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

import java.io.File;

/**
 * @Description: compress
 * @Author: yoga
 * @Date: 2023/7/4 13:55
 */
public interface Compress {

    /**
     * compress
     *
     * @param srcFile need compress source file
     * @param destDir compressed file dir
     */
    void compress(File srcFile, File destDir);
}
