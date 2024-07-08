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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.yoga.jarvis.cache.ServerInstanceCache;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.core.ServerInstance;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.plugin.Plugin;
import org.yoga.jarvis.plugin.PluginChain;
import org.yoga.jarvis.spi.LoadBalance;
import org.yoga.jarvis.spi.balance.LoadBalanceFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Route Plugin
 * @Author: yoga
 * @Date: 2024/6/18 15:30
 */
public class RoutePlugin implements Plugin {

    protected static final Logger logger = LoggerFactory.getLogger(RoutePlugin.class);

    /**
     * Gateway Server Configs
     */
    private ServerConfigs serverConfigs;

    public RoutePlugin(ServerConfigs serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public Integer order() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public String name() {
        return "Route";
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        return null;
    }

    private ServerInstance chooseInstance(String appName, ServerHttpRequest request) {
        List<ServerInstance> serverInstances = ServerInstanceCache.getServerInstances(appName);
        if (CollectionUtils.isEmpty(serverInstances)) {
            logger.error("server instance[{}] not find", appName);
            throw new JarvisException("server instance not find");
        }
        // version todo
        String version = "v1";
        //Select an instance based on the loadBalance algorithm
        LoadBalance loadBalance = LoadBalanceFactory.getInstance(serverConfigs.getLoadBalance(), appName, version);
        return loadBalance.select(serverInstances);
    }
}
