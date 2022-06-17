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

package org.yoga.jarvis.db;

import org.springframework.stereotype.Component;
import org.yoga.jarvis.entity.order.OrderId;

/**
 * @Description: OrderPersistenceHandler
 * @Author: yoga
 * @Date: 2022/6/14 23:02
 */
@Component
public class OrderDAO {

    /**
     * insert to db
     *
     * @param orderDO order do
     */
    public void insert(OrderDO orderDO) {

    }

    /**
     * update order
     *
     * @param orderDO order do
     */
    public void update(OrderDO orderDO) {

    }

    /**
     * select order by id
     *
     * @param id order id
     */
    public OrderDO selectById(OrderId id) {
        return new OrderDO();
    }

    /**
     * remove order by id
     *
     * @param id order id
     */
    public void removeById(OrderId id) {
    }
}
