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

package org.yoga.jarvis.entity.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.yoga.jarvis.base.Aggregate;
import org.yoga.jarvis.bussiness.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: Order Entity
 * @Author: yoga
 * @Date: 2022/6/11 21:04
 */
@Data
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class Order implements Aggregate<OrderId> {

	private OrderId id;

	/**
	 * 订单编号
	 */
	private String serialNo;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 订单状态
	 */
	private OrderStatus status;

	/**
	 * 订单总价
	 */
	private BigDecimal totalPrice;

	/**
	 * 订单实付价格
	 */
	private BigDecimal actualPrice;

	/**
	 * 订单备注
	 */
	private String remark;

	/**
	 * 订单创建时间
	 */
	private Date gmtCreated;

	/**
	 * 订单付款时间
	 */
	private Date gmtPaid;

	/**
	 * 订单发货时间
	 */
	private Date gmtDelivery;

	/**
	 * 订单成交时间
	 */
	private Date gmtCompleted;

}
