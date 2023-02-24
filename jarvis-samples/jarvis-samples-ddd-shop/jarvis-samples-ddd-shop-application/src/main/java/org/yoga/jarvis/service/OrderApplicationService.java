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

package org.yoga.jarvis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yoga.jarvis.cmd.CreateOrderAbilityCommand;
import org.yoga.jarvis.entity.order.Order;
import org.yoga.jarvis.entity.order.OrderId;
import org.yoga.jarvis.repository.OrderRepository;

/**
 * @Description: OrderService
 * @Author: yoga
 * @Date: 2022/6/23 19:31
 */
@Slf4j
@Service
public class OrderApplicationService {

	private final OrderRepository orderRepository;

	public OrderApplicationService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public OrderId createOrder(CreateOrderAbilityCommand command) {
		// 校验商品库存
		// todo
		Order order = new Order();
		orderRepository.save(order);
		return order.getId();
	}

}
