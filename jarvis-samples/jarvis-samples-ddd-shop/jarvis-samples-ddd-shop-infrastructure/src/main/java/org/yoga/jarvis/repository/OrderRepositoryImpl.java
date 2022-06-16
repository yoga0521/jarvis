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

package org.yoga.jarvis.repository;

import org.springframework.stereotype.Repository;
import org.yoga.jarvis.converter.OrderConverter;
import org.yoga.jarvis.db.OrderDAO;
import org.yoga.jarvis.entity.Order;
import org.yoga.jarvis.entity.OrderId;

import java.util.Objects;

/**
 * @Description: OrderRepositoryImpl
 * @Author: yoga
 * @Date: 2022/6/14 23:02
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDAO orderDAO;

    public OrderRepositoryImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


    @Override
    public void attach(Order aggregate) {

    }

    @Override
    public void detach(Order aggregate) {

    }

    @Override
    public Order find(OrderId orderId) {
        return null;
    }

    @Override
    public void remove(Order aggregate) {

    }

    @Override
    public void save(Order order) {
        if (Objects.isNull(order.getId())) {
            orderDAO.insert(OrderConverter.INSTANCE.toDO(order));
        } else {
            orderDAO.update(OrderConverter.INSTANCE.toDO(order));
        }
    }
}
