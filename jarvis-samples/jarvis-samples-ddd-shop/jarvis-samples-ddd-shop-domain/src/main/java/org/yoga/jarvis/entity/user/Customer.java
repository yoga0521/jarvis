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

package org.yoga.jarvis.entity.user;

import lombok.Data;
import org.yoga.jarvis.base.Aggregate;

/**
 * @Description: customer
 * @Author: yoga
 * @Date: 2022/6/17 22:51
 */
@Data
public class Customer implements Aggregate<CustomerId> {

    private String name;


    @Override
    public CustomerId getId() {
        return new CustomerId();
    }
}
