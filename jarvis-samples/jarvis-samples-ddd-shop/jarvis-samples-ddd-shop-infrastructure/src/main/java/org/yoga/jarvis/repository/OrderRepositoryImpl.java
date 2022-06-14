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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yoga.jarvis.base.Aggregate;
import org.yoga.jarvis.base.MarkerInterface;
import org.yoga.jarvis.db.OrderPersistenceHandler;

/**
 * @Description: OrderRepositoryImpl
 * @Author: yoga
 * @Date: 2022/6/14 23:02
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderPersistenceHandler orderPersistenceHandler;

    public OrderRepositoryImpl(OrderPersistenceHandler orderPersistenceHandler) {
        this.orderPersistenceHandler = orderPersistenceHandler;
    }

    @Override
    public void attach(Aggregate aggregate) {

    }

    @Override
    public void detach(Aggregate aggregate) {

    }

    @Override
    public Aggregate find(MarkerInterface markerInterface) {
        return null;
    }

    @Override
    public void remove(Aggregate aggregate) {

    }

    @Override
    public void save(Aggregate aggregate) {

    }
}
