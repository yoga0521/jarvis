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

package org.yoga.jarvis.base;

/**
 * @Description: Repository
 * @Author: yoga
 * @Date: 2022/6/8 19:47
 */
public interface Repository<T extends Aggregate<ID>, ID extends MarkerInterface> {

    /**
     * Attach an Aggregate to a Repository and make it tracking
     *
     * @param aggregate Aggregate
     */
    void attach(T aggregate);

    /**
     * Detach an Aggregate and lift the tracking
     *
     * @param aggregate Aggregate
     */
    void detach(T aggregate);

    /**
     * Find an Aggregate by ID
     *
     * @param id ID
     * @return Aggregate
     */
    T find(ID id);

    /**
     * Remove an Aggregate and automatically lift the tracking
     *
     * @param aggregate Aggregate
     */
    void remove(T aggregate);

    /**
     * Save an Aggregate and automatically reset the tracking
     *
     * @param aggregate Aggregate
     */
    void save(T aggregate);
}
