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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Description:
 * @Author: yoga
 * @Date: 2024/5/31 15:14
 */
public class PluginChain implements Plugin {

    /**
     * the plugins of chain
     */
    private List<Plugin> plugins;

    @Override
    public Integer order() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    protected void execute() {

    }

    /**
     * add plugin to chain
     *
     * @param plugin plugin
     */
    public void addPlugin(Plugin plugin) {
        if (plugins == null) {
            plugins = new ArrayList<>();
        }
        plugins.add(plugin);
        // order by the plugin's order
        plugins.sort(Comparator.comparing(Plugin::order));
    }
}
