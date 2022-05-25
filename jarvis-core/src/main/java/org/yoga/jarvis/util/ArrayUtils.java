/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis.util;

import java.util.Arrays;

/**
 * @Description: Array Utils
 * @Author: yoga
 * @Date: 2022/5/16 15:46
 */
public class ArrayUtils {

    /**
     * Check an array is {@code null} or has length 0
     *
     * <pre class="code">
     * ArrayUtils.isEmpty({@code null}) = true
     * ArrayUtils.isEmpty(new String[] {0}) = true
     * ArrayUtils.isEmpty(new String[] {"JARVIS"}) = false
     * </pre>
     *
     * @param array the array
     * @return {@code true} if the array is {@code null} or has length 0
     */
    public static <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check an array is not {@code null} and has length
     *
     * <pre class="code">
     * ArrayUtils.isNotEmpty({@code null}) = false
     * ArrayUtils.isNotEmpty(new String[] {0}) = false
     * ArrayUtils.isNotEmpty(new String[] {"JARVIS"}) = true
     * </pre>
     *
     * @param array the array
     * @return {@code true} if the array is not {@code null} and has length
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }


    public static <T> int indexOf(T[] array, T obj) {
        if (isEmpty(array)) {
            return -1;
        }
        if (obj == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                if (obj.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static <T> boolean contains(T[] array, T obj) {
        return indexOf(array, obj) != -1;
    }

    /**
     * Concatenate char arrays
     *
     * @param firstChars first char array
     * @param arrayChars other char array
     * @return concatenated char array
     */
    public static char[] concatAll(char[] firstChars, char[]... arrayChars) {
        if (null == firstChars && null == arrayChars) {
            return null;
        } else if (isEmpty(arrayChars)) {
            return firstChars;
        }
        if (null == firstChars) {
            firstChars = new char[]{0};
        }
        int totalLength = firstChars.length;
        for (char[] array : arrayChars) {
            if (null == array) {
                continue;
            }
            totalLength += array.length;
        }
        char[] result = Arrays.copyOf(firstChars, totalLength);
        int offset = firstChars.length;
        for (char[] array : arrayChars) {
            if (null == array) {
                continue;
            }
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
