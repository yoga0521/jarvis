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
 * @Description: NumberUtils
 * @Author: yoga
 * @Date: 2022/9/2 17:42
 */
public class NumberUtils {

    /**
     * whether it is a integer
     *
     * @param s str
     * @return whether it is a integer
     */
    public static boolean isInteger(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * whether it is a long
     *
     * @param s str
     * @return whether it is a long
     */
    public static boolean isLong(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * whether it is a double
     *
     * @param s str
     * @return whether it is a double
     */
    public static boolean isDouble(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException ignore) {
            return false;
        }
        return s.contains(".");
    }
}
