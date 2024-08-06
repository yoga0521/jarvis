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

/**
 * @Description: Application Route Rule
 * @Author: yoga
 * @Date: 2024/8/6 14:26
 */
public class ApplicationRouteRule {

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
