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

package org.yoga.jarvis.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.yoga.jarvis.dto.OrderDTO;
import org.yoga.jarvis.entity.order.Order;

/**
 * @Description: order assembler
 * @Author: yoga
 * @Date: 2022/6/19 17:42
 */
@Mapper
public interface OrderAssembler {

	OrderAssembler INSTANCE = Mappers.getMapper(OrderAssembler.class);

	/**
	 * entity to dto
	 *
	 * @param order order entity
	 * @return order dto
	 */
	@Mappings({
			@Mapping(expression = "java(order.getStatus() == null ? null : order.getStatus().name())", target = "status")
	})
	OrderDTO toDTO(Order order);

	/**
	 * dto to entity
	 *
	 * @param orderDTO order dto
	 * @return order entity
	 */
	@Mappings({
			@Mapping(expression = "java(OrderStatus.getOrderStatusByName(orderDTO.getStatus()))", target = "status")
	})
	Order toEntity(OrderDTO orderDTO);
}
