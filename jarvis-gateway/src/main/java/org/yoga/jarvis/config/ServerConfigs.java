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

package org.yoga.jarvis.config;

import org.yoga.jarvis.constant.RateLimiterType;

/**
 * @Description: Gateway Server Configs
 * @Author: yoga
 * @Date: 2024/6/6 15:36
 */
public class ServerConfigs {

    /**
     * The gateway timeout(ms), default 5 seconds
     */
    private long timeout = 5 * 1000L;

    /**
     * Cache refresh interval, default 10 seconds
     */
    private Long cacheRefreshInterval = 10 * 1000L;

    /**
     * Limiter Type {@link RateLimiterType}, default TokenBucketLimiter
     */
    private RateLimiterType rateLimiterType = RateLimiterType.Token_Bucket;

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Long getCacheRefreshInterval() {
        return cacheRefreshInterval;
    }

    public void setCacheRefreshInterval(Long cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    public RateLimiterType getRateLimiterType() {
        return rateLimiterType;
    }

    public void setRateLimiterType(RateLimiterType rateLimiterType) {
        this.rateLimiterType = rateLimiterType;
    }
}
