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
 * @Description: ObjectUtils
 * @Author: yoga
 * @Date: 2022/8/19 10:53
 */
public class ObjectUtils {

    /**
     * Parses the object argument as a boolean.  The {@code boolean}
     * returned represents the value {@code true} if the object argument
     * is not {@code null} and is equal, ignoring case, to the object
     * {@code "true"}. <p>
     * Example: {@code ObjectUtils.parseBoolean("True")} returns {@code true}.<br>
     * Example: {@code ObjectUtils.parseBoolean("yes")} returns {@code false}.
     *
     * @param obj the {@code Object} containing the boolean
     *            representation to be parsed
     * @return the boolean represented by the object argument
     */
    public static Boolean parseBoolean(Object obj) {
        return Boolean.valueOf(String.valueOf(obj));
    }

    /**
     * Parses the object argument as a signed decimal {@code
     * short}. The characters in the object must all be decimal
     * digits, except that the first character may be an ASCII minus
     * sign {@code '-'} ({@code '\u005Cu002D'}) to indicate a
     * negative value or an ASCII plus sign {@code '+'}
     * ({@code '\u005Cu002B'}) to indicate a positive value.
     *
     * @param obj a {@code Object} containing the {@code short}
     *            representation to be parsed
     * @return the {@code short} value represented by the argument in decimal.
     * @throws NumberFormatException If the object does not
     *                               contain a parsable {@code short}.
     */
    public static Short parseShort(Object obj) throws NumberFormatException {
        Assert.notNull(obj, "obj must not be null!");
        return Short.valueOf(String.valueOf(obj));
    }

    /**
     * Parses the object as a signed decimal integer. The
     * characters in the object must all be decimal digits, except
     * that the first character may be an ASCII minus sign {@code '-'}
     * ({@code '\u005Cu002D'}) to indicate a negative value or an
     * ASCII plus sign {@code '+'} ({@code '\u005Cu002B'}) to
     * indicate a positive value.
     *
     * @param obj a {@code Object} containing the {@code int}
     *            representation to be parsed
     * @return the integer value represented by the argument in decimal.
     * @throws NumberFormatException if the object does not contain a
     *                               parsable integer.
     */
    public static Integer parseInt(Object obj) throws NumberFormatException {
        Assert.notNull(obj, "obj must not be null!");
        return Integer.valueOf(String.valueOf(obj));
    }

    /**
     * Parses the object argument as a signed decimal {@code long}.
     * The characters in the object must all be decimal digits, except
     * that the first character may be an ASCII minus sign {@code '-'}
     * ({@code \u005Cu002D'}) to indicate a negative value or an
     * ASCII plus sign {@code '+'} ({@code '\u005Cu002B'}) to
     * indicate a positive value.
     *
     * <p>Note that neither the character {@code L}
     * ({@code '\u005Cu004C'}) nor {@code l}
     * ({@code '\u005Cu006C'}) is permitted to appear at the end
     * of the object as a type indicator, as would be permitted in
     * Java programming language source code.
     *
     * @param obj a {@code Object} containing the {@code long}
     *            representation to be parsed
     * @return the {@code long} represented by the argument in decimal.
     * @throws NumberFormatException if the object does not contain a
     *                               parsable {@code long}.
     */
    public static Long parseLong(Object obj) throws NumberFormatException {
        Assert.notNull(obj, "obj must not be null!");
        return Long.valueOf(String.valueOf(obj));
    }

    /**
     * Returns a {@code Float} object holding the
     * {@code float} value represented by the argument object
     * {@code obj}.
     *
     * @param obj the object to be parsed.
     * @return a {@code Float} object holding the value represented by the {@code Object} argument.
     * @throws NumberFormatException if the object does not contain a
     *                               parsable number.
     */
    public static Float parseFloat(Object obj) throws NumberFormatException {
        Assert.notNull(obj, "obj must not be null!");
        return Float.valueOf(String.valueOf(obj));
    }

    /**
     * Returns a {@code Double} object holding the
     * {@code double} value represented by the argument object
     * {@code obj}.
     *
     * @param obj the object to be parsed.
     * @return a {@code Double} object holding the value represented by the {@code Object} argument.
     * @throws NumberFormatException if the object does not contain a
     *                               parsable number.
     */
    public static Double parseDouble(Object obj) throws NumberFormatException {
        Assert.notNull(obj, "obj must not be null!");
        return Double.valueOf(String.valueOf(obj));
    }


}
