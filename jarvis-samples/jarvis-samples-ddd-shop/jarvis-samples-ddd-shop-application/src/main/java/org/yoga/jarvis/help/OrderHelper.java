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

package org.yoga.jarvis.help;

import org.yoga.jarvis.util.RandomStringUtils;

/**
 * @Description: 订单辅助类
 * @Author: yoga
 * @Date: 2023/2/24 14:46
 */
public class OrderHelper {

	/**
	 * 生成流水号
	 * 比较严格的方法是通过数据库递增字段进行生成
	 *
	 * @return 流水号
	 */
	public static String generateSerialNo() {
		return System.nanoTime() * 3 + RandomStringUtils.randomNumeric(6);
	}
}
