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

package org.yoga.jarvis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoga.jarvis.bean.Result;
import org.yoga.jarvis.cmd.CreateOrderAbilityCommand;
import org.yoga.jarvis.entity.order.OrderId;
import org.yoga.jarvis.service.OrderApplicationService;

/**
 * @Description: OrderController
 * @Author: yoga
 * @Date: 2022/6/21 17:42
 */
@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public Result<OrderId> create(@RequestBody CreateOrderAbilityCommand command) {

        return Result.success(null);
    }

}
