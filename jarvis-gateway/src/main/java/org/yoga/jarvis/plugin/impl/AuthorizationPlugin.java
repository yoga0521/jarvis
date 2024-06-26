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

package org.yoga.jarvis.plugin.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.plugin.Plugin;
import org.yoga.jarvis.plugin.PluginChain;
import reactor.core.publisher.Mono;

/**
 * @Description: Authorization Plugin
 * @Author: yoga
 * @Date: 2024/6/7 15:28
 */
public class AuthorizationPlugin implements Plugin {

    protected static final Logger logger = LoggerFactory.getLogger(AuthorizationPlugin.class);

    /**
     * Gateway Server Configs
     */
    private ServerConfigs serverConfigs;

    public AuthorizationPlugin(ServerConfigs serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public Integer order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String name() {
        return "Authorization";
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        logger.info("authorization plugin start execute");
        return pluginChain.execute(exchange, pluginChain);
    }
}
