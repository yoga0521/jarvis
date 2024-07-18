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

package org.yoga.jarvis.chain;

import org.springframework.web.server.ServerWebExchange;
import org.yoga.jarvis.plugin.Plugin;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Description: PluginChain
 * @Author: yoga
 * @Date: 2024/5/31 15:14
 */
public class PluginChain {

    /**
     * App Name
     */
    private final String appName;

    /**
     * The position of current plugin
     */
    private int pos = 0;

    /**
     * the plugins of chain
     */
    private final List<Plugin> plugins;

    public PluginChain(String appName, List<Plugin> plugins) {
        this.appName = appName;
        plugins.sort(Comparator.comparing(Plugin::order));
        this.plugins = Collections.unmodifiableList(plugins);
    }

    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        if (pos == plugins.size()) {
            return exchange.getResponse().setComplete();
        }
        return pluginChain.plugins.get(pos++).execute(exchange, this);
    }

    public String getAppName() {
        return appName;
    }
}
