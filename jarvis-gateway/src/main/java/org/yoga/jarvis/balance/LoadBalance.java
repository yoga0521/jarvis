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

import org.yoga.jarvis.core.ServerInstance;
import org.yoga.jarvis.exception.JarvisException;

import java.util.List;

/**
 * @Description: Load Balance
 * @Author: yoga
 * @Date: 2024/6/12 15:20
 */
public interface LoadBalance {

    /**
     * select load balance
     *
     * @param instances server instances
     * @return The server instance of the selected
     * @throws JarvisException Exception
     */
    ServerInstance select(List<ServerInstance> instances) throws JarvisException;

    /**
     * get the name of the load balance policy
     *
     * @return the name of the load balance policy
     */
    String getName();
}
