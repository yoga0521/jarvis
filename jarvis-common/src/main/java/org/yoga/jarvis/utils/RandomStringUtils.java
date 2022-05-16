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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: Random String Utils
 * @Author: yoga
 * @Date: 2022/5/16 15:38
 */
public class RandomStringUtils {

    /**
     * numeric char array
     */
    private static final char[] NUMERIC_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * lower case alphabetic char array
     */
    private static final char[] LOWER_CASE_ALPHABETIC_CHARS = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * upper case alphabetic char array
     */
    private static final char[] UPPER_CASE_ALPHABETIC_CHARS = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * lower case alphanumeric char array
     */
    private static final char[] LOWER_CASE_ALPHANUMERIC_CHARS = ArrayUtils.concatAll(NUMERIC_CHARS, LOWER_CASE_ALPHABETIC_CHARS);

    /**
     * upper case alphanumeric char array
     */
    private static final char[] UPPER_CASE_ALPHANUMERIC_CHARS = ArrayUtils.concatAll(NUMERIC_CHARS, UPPER_CASE_ALPHABETIC_CHARS);

    /**
     * alphabetic char array
     */
    private static final char[] ALPHABETIC_CHARS = ArrayUtils.concatAll(LOWER_CASE_ALPHABETIC_CHARS, UPPER_CASE_ALPHABETIC_CHARS);

    /**
     * alphanumeric char array
     */
    private static final char[] ALPHANUMERIC_CHARS = ArrayUtils.concatAll(NUMERIC_CHARS, LOWER_CASE_ALPHABETIC_CHARS, UPPER_CASE_ALPHABETIC_CHARS);

    /**
     * easy to confuse char array
     */
    private static final char[] EASY_TO_CONFUSE_CHARS = new char[]{'o', 'O', 'I'};

    /**
     * Generate a random string containing numeric
     *
     * @param length random string length
     * @return random string
     */
    public static String randomNumeric(int length) {
        return random(NUMERIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing numeric, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomNumericExcludedChars(int length, List<Character> excludedChars) {
        return random(NUMERIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing lower case alphabetic
     *
     * @param length random string length
     * @return random string
     */
    public static String randomLowerCaseAlphabetic(int length) {
        return random(LOWER_CASE_ALPHABETIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing lower case alphabetic, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomLowerCaseAlphabeticExcludedChars(int length, List<Character> excludedChars) {
        return random(LOWER_CASE_ALPHABETIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing upper case alphabetic
     *
     * @param length random string length
     * @return random string
     */
    public static String randomUpperCaseAlphabetic(int length) {
        return random(UPPER_CASE_ALPHABETIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing upper case alphabetic, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomUpperCaseAlphabeticExcludedChars(int length, List<Character> excludedChars) {
        return random(UPPER_CASE_ALPHABETIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing lower case alphanumeric
     *
     * @param length random string length
     * @return random string
     */
    public static String randomLowerCaseAlphanumeric(int length) {
        return random(LOWER_CASE_ALPHANUMERIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing lower case alphanumeric, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomLowerCaseAlphanumericExcludedChars(int length, List<Character> excludedChars) {
        return random(LOWER_CASE_ALPHANUMERIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing upper case alphanumeric
     *
     * @param length random string length
     * @return random string
     */
    public static String randomUpperCaseAlphanumeric(int length) {
        return random(UPPER_CASE_ALPHANUMERIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing upper case alphanumeric, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomUpperCaseAlphanumericExcludedChars(int length, List<Character> excludedChars) {
        return random(UPPER_CASE_ALPHANUMERIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing alphabetic
     *
     * @param length random string length
     * @return random string
     */
    public static String randomAlphabetic(int length) {
        return random(ALPHABETIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing alphabetic, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomAlphabeticExcludedChars(int length, List<Character> excludedChars) {
        return random(ALPHABETIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string containing alphanumeric
     *
     * @param length random string length
     * @return random string
     */
    public static String randomAlphanumeric(int length) {
        return random(ALPHANUMERIC_CHARS, length, null);
    }

    /**
     * Generate a random string containing alphanumeric, exclude the specified characters
     *
     * @param length        random string length
     * @param excludedChars specified characters
     * @return random string
     */
    public static String randomAlphanumericExcludedChars(int length, List<Character> excludedChars) {
        return random(ALPHANUMERIC_CHARS, length, excludedChars);
    }

    /**
     * Generate a random string
     *
     * @param chars         char array
     * @param length        random string length
     * @param excludedChars exclude char list
     * @return random string
     */
    public static String random(char[] chars, int length, List<Character> excludedChars) {
        Assert.notEmpty(chars, "chars must not be empty");
        Assert.isTrue(1 < length, String.format("length[%d] is illegal", length));

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = chars[threadLocalRandom.nextInt(chars.length)];
            while (!CollectionUtils.isEmpty(excludedChars) && excludedChars.contains(randomChar)) {
                randomChar = chars[threadLocalRandom.nextInt(chars.length)];
            }
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}
