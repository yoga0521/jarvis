/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis.balance;

import org.yoga.jarvis.CacheHandler;
import org.yoga.jarvis.GuavaCacheHandler;

/**
 * @Description: Load Balance Factory
 * @Author: yoga
 * @Date: 2024/6/21 17:07
 */
public class LoadBalanceFactory {

    private static final CacheHandler<String, LoadBalance> LOAD_BALANCE_CACHE = new GuavaCacheHandler<>(8, 32, 24 * 60 * 60);

    private LoadBalanceFactory() {
    }
}
