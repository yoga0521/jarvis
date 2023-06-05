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

package org.yoga.jarvis.util;

/**
 * @Description: OS Utils
 * @Author: yoga
 * @Date: 2023/6/5 17:48
 */
public class OSUtils {

    /**
     * windows
     */
    public static final String WINDOWS = "Windows";

    /**
     * mac
     */
    public static final String MAC = "Mac OS";

    /**
     * is Windows OS
     *
     * @return is Windows OS
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").contains(WINDOWS);
    }

    /**
     * is Mac OS
     *
     * @return is Mac OS
     */
    public static boolean isMAC() {
        return System.getProperty("os.name").contains(MAC);
    }

}
