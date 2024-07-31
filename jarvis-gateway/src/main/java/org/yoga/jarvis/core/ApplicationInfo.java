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

package org.yoga.jarvis.core;

import java.util.List;

/**
 * @Description: Application Info
 * @Author: yoga
 * @Date: 2024/7/29 17:27
 */
public class ApplicationInfo {

    /**
     * application ID
     */
    private String appId;

    /**
     * application name
     */
    private String appName;

    /**
     * application server instances
     */
    private List<ServerInstance> serverInstances;

    /**
     * route rule
     */
    private RouteRule routeRule;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<ServerInstance> getServerInstances() {
        return serverInstances;
    }

    public void setServerInstances(List<ServerInstance> serverInstances) {
        this.serverInstances = serverInstances;
    }

    public RouteRule getRouteRule() {
        return routeRule;
    }

    public void setRouteRule(RouteRule routeRule) {
        this.routeRule = routeRule;
    }

    public static class RouteRule {

        /**
         * application version
         */
        private String version;

        /**
         * priority
         */
        private int priority;

        /**
         * match key
         */
        private String matchKey;

        /**
         * match method
         */
        private String matchMethod;

        /**
         * match rule
         */
        private String matchRule;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getMatchKey() {
            return matchKey;
        }

        public void setMatchKey(String matchKey) {
            this.matchKey = matchKey;
        }

        public String getMatchMethod() {
            return matchMethod;
        }

        public void setMatchMethod(String matchMethod) {
            this.matchMethod = matchMethod;
        }

        public String getMatchRule() {
            return matchRule;
        }

        public void setMatchRule(String matchRule) {
            this.matchRule = matchRule;
        }
    }
}
