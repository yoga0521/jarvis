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

import java.util.regex.Pattern;

/**
 * @Description: Pattern utils
 * @Author: yoga
 * @Date: 2023/4/23 16:31
 */
public class PatternUtils {

    /**
     * Number
     */
    public static final Pattern NUMBER = Pattern.compile("\\d+");

    /**
     * Letter
     */
    public static final Pattern LETTER = Pattern.compile("[a-zA-Z]+");

    /**
     * Chinese
     */
    public static final Pattern CHINESE = Pattern.compile("[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]");

    /**
     * ID Card
     */
    public static final Pattern ID_CARD = Pattern.compile("\\d{6}((((((19|20)\\d{2})(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(((19|20)\\d{2})(0[13578]|1[02])31)|((19|20)\\d{2})02(0[1-9]|1\\d|2[0-8])|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))0229))\\d{3})|((((\\d{2})(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|((\\d{2})(0[13578]|1[02])31)|((\\d{2})02(0[1-9]|1\\d|2[0-8]))|(([13579][26]|[2468][048]|0[048])0229))\\d{2}))(\\d|X|x)");

    /**
     * Hong Kong ID Card
     */
    public static final Pattern ID_CARD_HK = Pattern.compile("[a-zA-Z]\\d{6}\\([\\dA]\\)");

    /**
     * Macau ID Card
     */
    public static final Pattern ID_CARD_MO = Pattern.compile("[1|5|7]\\d{6}\\(\\d\\)");

    /**
     * Taiwan ID Card
     */
    public static final Pattern ID_CARD_TW = Pattern.compile("[a-zA-Z][0-9]{9}");

    /**
     * Credit Code
     */
    public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

    /**
     * Mobile
     */
    public static final Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3-9]\\d{9}");

    /**
     * Hong Kong Mobile
     */
    public static final Pattern MOBILE_HK = Pattern.compile("(?:0|852|\\+852)?\\d{8}");

    /**
     * Taiwan Mobile
     */
    public static final Pattern MOBILE_TW = Pattern.compile("(?:0|886|\\+886)?(?:|-)09\\d{8}");

    /**
     * Macau Mobile
     */
    public static final Pattern MOBILE_MO = Pattern.compile("(?:0|853|\\+853)?(?:|-)6\\d{7}");

    /**
     * Landline number
     */
    public static final Pattern TEL = Pattern.compile("(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})");

    /**
     * email
     */
    public static final Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Pattern.CASE_INSENSITIVE);

    /**
     * mac address
     */
    public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER", Pattern.CASE_INSENSITIVE);

    /**
     * strong password
     * At least 6 characters, including at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character
     */
    public static final Pattern STRONG_PASSWORD = Pattern.compile("\\S*(?=\\S{8,})(?=\\S*\\d)(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*[!@#$%^&*? ])\\S*");


    public static boolean fuzzyMatch(String pattern, String str) {
        if (StringUtils.isBlank(pattern) || str == null) {
            return false;
        }

        if (!pattern.contains("*")) {
            return pattern.equals(str);
        }

        if (pattern.startsWith("*")) {
            // pattern:*
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf("*", 1);
            // pattern:*xy
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            // pattern:**
            if (part.isEmpty()) {
                return fuzzyMatch(pattern.substring(nextIndex), str);
            }
            int partIdx = str.indexOf(part);
            while (partIdx != -1) {
                if (fuzzyMatch(pattern.substring(nextIndex), str.substring(partIdx + part.length()))) {
                    return true;
                }
                partIdx = str.indexOf(part, partIdx + 1);
            }
            return false;
        }

        int idx = pattern.indexOf("*");
        // second judgment: check the front part of the asterisk is equal
        // third judgment: asterisk as well as the following part(example:*xy) is fuzzy match
        return str.length() >= idx && pattern.startsWith(str.substring(0, idx)) && fuzzyMatch(pattern.substring(idx), str.substring(idx));
    }
}
