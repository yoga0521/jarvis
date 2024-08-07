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
import org.yoga.jarvis.core.ApplicationRouteRule;

import java.util.List;
import java.util.Map;

/**
 * @Description: Application Route Rule Cache
 * @Author: yoga
 * @Date: 2024/8/6 14:39
 */
public class ApplicationRouteRuleCache {

    private static final CacheHandler<String, List<ApplicationRouteRule>> APPLICATION_ROUTE_RULE_CACHE = new GuavaCacheHandler<>(8, 256, 24 * 60 * 60);

    /**
     * add application route rules to cache
     *
     * @param applicationName application name
     * @param rules           application route rules
     */
    public static void add(String applicationName, List<ApplicationRouteRule> rules) {
        APPLICATION_ROUTE_RULE_CACHE.put(applicationName, rules);
    }

    /**
     * add application route rule map to cache
     *
     * @param ruleMap application route rule map
     */
    public static void addAll(Map<String, List<ApplicationRouteRule>> ruleMap) {
        ruleMap.forEach(ApplicationRouteRuleCache::add);
    }

    /**
     * get application route rules by application name
     *
     * @param applicationName application name
     * @return application route rules
     */
    public static List<ApplicationRouteRule> getApplicationRouteRules(String applicationName) {
        return APPLICATION_ROUTE_RULE_CACHE.getIfPresent(applicationName);
    }
}
