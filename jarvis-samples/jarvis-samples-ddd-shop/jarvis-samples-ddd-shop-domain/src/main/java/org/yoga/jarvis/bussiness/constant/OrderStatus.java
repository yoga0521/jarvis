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

package org.yoga.jarvis.bussiness.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: 订单状态
 * @Author: yoga
 * @Date: 2023/2/28 15:15
 */
@Getter
public enum OrderStatus {

	/**
	 * 待付款
	 */
	wait_pay("待付款"),

	/**
	 * 待发货
	 */
	wait_deliver("待发货"),

	/**
	 * 待收货
	 */
	wait_receive("待收货"),

	/**
	 * 待评价
	 */
	wait_evaluate("待评价"),

	/**
	 * 已完成
	 */
	completed("已完成"),

	/**
	 * 已取消
	 */
	cancelled("已取消");

	/**
	 * 订单状态描述
	 */
	private final String desc;

	OrderStatus(String desc) {
		this.desc = desc;
	}

	/**
	 * 订单状态map
	 */
	private static final Map<String, OrderStatus> ORDER_STATUS_MAP = new HashMap<>(16);

	static {
		for (OrderStatus status : OrderStatus.values()) {
			ORDER_STATUS_MAP.put(status.name(), status);
		}
	}

	/**
	 * 根据订单状态枚举名称获取订单状态枚举
	 *
	 * @param name 订单状态枚举名称
	 * @return 订单状态枚举
	 */
	public static OrderStatus getOrderStatusByName(String name) {
		return ORDER_STATUS_MAP.get(name);
	}

	/**
	 * 根据订单状态枚举名称获取订单状态描述
	 *
	 * @param name 订单状态枚举名称
	 * @return 订单状态描述
	 */
	public static String getOrderDescByName(String name) {
		return Optional.ofNullable(getOrderStatusByName(name)).map(OrderStatus::getDesc).orElse(null);
	}
}
