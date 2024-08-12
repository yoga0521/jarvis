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

package org.yoga.jarvis.cache;

import org.yoga.jarvis.CacheHandler;
import org.yoga.jarvis.GuavaCacheHandler;
import org.yoga.jarvis.core.ServerInstance;

import java.util.List;

/**
 * @Description: Server Instance Cache
 * @Author: yoga
 * @Date: 2024/7/1 14:36
 */
public class ServerInstanceCache {

    private static final CacheHandler<String, List<ServerInstance>> SERVER_INSTANCE_CACHE = new GuavaCacheHandler<>(8, 1024, 24 * 60 * 60);

    /**
     * get instances by server name
     *
     * @param serverName server name
     * @return instances
     */
    public static List<ServerInstance> getServerInstances(String serverName) {
        return SERVER_INSTANCE_CACHE.getIfPresent(serverName);
    }

    /**
     * add server instances to cache
     *
     * @param serverName server name
     * @param instances  server instances
     */
    public static void add(String serverName, List<ServerInstance> instances) {
        SERVER_INSTANCE_CACHE.put(serverName, instances);
    }

    /**
     * remove the expired server
     *
     * @param onlineServerAppNames online server app name
     */
    public static void removeExpired(List<String> onlineServerAppNames) {
        SERVER_INSTANCE_CACHE.removeIf(key -> !onlineServerAppNames.contains(key));
    }

}
