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
	public static final Pattern CHINESE = Pattern.compile("[⺀-\u2eff⼀-\u2fdf㇀-\u31ef㐀-䶿一-\u9fff豈-\ufaff\ud840\udc00-\ud869\udedf\ud869\udf00-\ud86d\udf3f\ud86d\udf40-\ud86e\udc1f\ud86e\udc20-\ud873\udeaf\ud87e\udc00-\ud87e\ude1f]");

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
	public static final Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", 2);

	/**
	 * mac address
	 */
	public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER", 2);

	/**
	 * strong password
	 * At least 6 characters, including at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character
	 */
	public static final Pattern STRONG_PASSWORD = Pattern.compile("\\S*(?=\\S{8,})(?=\\S*\\d)(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*[!@#$%^&*? ])\\S*");

}
