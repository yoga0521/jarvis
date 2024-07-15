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

package org.yoga.jarvis.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: Plugin
 * @Author: yoga
 * @Date: 2024/5/30 16:46
 */
public interface Plugin {

    /**
     * order val
     * lower val have higher priority
     *
     * @return order val
     */
    Integer order();

    /**
     * plugin name
     *
     * @return plugin name
     */
    String name();

    /**
     * plugin execute
     *
     * @param exchange    ServerWebExchange
     * @param pluginChain PluginChain
     * @return execute result
     */
    Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain);
}
