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

package org.yoga.jarvis.filter;

import org.springframework.http.server.RequestPath;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.plugin.Plugin;
import org.yoga.jarvis.chain.PluginChain;
import org.yoga.jarvis.plugin.impl.AuthorizationPlugin;
import org.yoga.jarvis.plugin.impl.RoutePlugin;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Plugin Filter
 * @Author: yoga
 * @Date: 2024/6/5 17:08
 */
public class PluginFilter implements WebFilter {

    /**
     * Gateway Server Configs
     */
    private final ServerConfigs serverConfigs;

    public PluginFilter(ServerConfigs serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        RequestPath requestPath = exchange.getRequest().getPath();
        // request path format: /[appName]/path
        String appName = requestPath.value().split("/")[1];
        List<Plugin> plugins = new ArrayList<>();
        plugins.add(new AuthorizationPlugin(serverConfigs));
        plugins.add(new RoutePlugin(serverConfigs));
        PluginChain pluginChain = new PluginChain(appName, plugins);
        return pluginChain.execute(exchange, pluginChain);
    }
}
