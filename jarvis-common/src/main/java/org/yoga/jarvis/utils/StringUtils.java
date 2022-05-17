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

package org.yoga.jarvis.utils;

/**
 * @Description: String Utils
 * @Author: yoga
 * @Date: 2022/5/16 13:34
 */
public class StringUtils {

    /**
     * Check the {@code CharSequence} is {@code null} or has length 0
     *
     * <p>
     * Note: this method returns {@code false} for a {@code CharSequence} there are whitespace
     * <p>
     * <pre class="code">
     * StringUtils.isEmpty({@code null}) = true
     * StringUtils.isEmpty("") = true
     * StringUtils.isEmpty(" ") = false
     * StringUtils.isEmpty("JARVIS") = false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check, nullable
     * @return {@code true} if the {@code CharSequence} is {@code null} or has length 0
     * @see #isBlank (CharSequence)
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Check the {@code CharSequence} is not {@code null} and has length
     *
     * <p>
     * Note: this method returns {@code true} for a {@code CharSequence} there are whitespace
     * <p>
     * <pre class="code">
     * StringUtils.isNotEmpty({@code null}) = false
     * StringUtils.isNotEmpty("") = false
     * StringUtils.isNotEmpty(" ") = true
     * StringUtils.isNotEmpty("JARVIS") = true
     * </pre>
     *
     * @param cs the {@code CharSequence} to check, nullable
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     * @see #isNotBlank (CharSequence)
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * Check the {@code CharSequence} is {@code null} or has length 0 or has whitespace only
     *
     * <p>
     * Note: this method returns {@code true} for a {@code CharSequence} has whitespace only
     * <p>
     * <pre class="code">
     * StringUtils.isBlank({@code null}) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank("  ") = true
     * StringUtils.isBlank("JARVIS") = false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check, nullable
     * @return {@code true} if the {@code CharSequence} is {@code null} or has length 0 or has whitespace only
     * @see #isEmpty (CharSequence)
     */
    public static boolean isBlank(final CharSequence cs) {
        if (isEmpty(cs)) {
            return true;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the {@code CharSequence} is not {@code null} and length greater 0 and not whitespace only
     *
     * <p>
     * Note: this method returns {@code false} for a {@code CharSequence} has whitespace only
     * <p>
     * <pre class="code">
     * StringUtils.isNotBlank({@code null}) = false
     * StringUtils.isNotBlank("") = false
     * StringUtils.isNotBlank("  ") = false
     * StringUtils.isNotBlank("JARVIS") = true
     * </pre>
     *
     * @param cs the {@code CharSequence} to check, nullable
     * @return {@code true} if not {@code null} and length greater 0 and not whitespace only
     * @see #isNotEmpty (CharSequence)
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static String removeStart(final String str, final String removeStr) {
        if (isEmpty(str) || isEmpty(removeStr)) {
            return str;
        }
        if (str.startsWith(removeStr)) {
            return str.substring(removeStr.length());
        }
        return str;
    }


}
