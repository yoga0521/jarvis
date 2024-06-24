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

package org.yoga.jarvis.balance.impl;

import org.yoga.jarvis.balance.AbstractLoadBalance;
import org.yoga.jarvis.core.ServerInstance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: Random Load Balance
 * @Author: yoga
 * @Date: 2024/6/13 17:47
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "random";

    @Override
    protected ServerInstance doSelect(List<ServerInstance> instances) {
        return instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
