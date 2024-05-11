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

package org.yoga.jarvis.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: yoga
 * @Date: 2024/5/11 16:09
 */
public class TokenBucketLimiter {

    private static final Logger logger = LoggerFactory.getLogger(TokenBucketLimiter.class);

    /**
     * capacity
     */
    private final int capacity;

    /**
     * tokens per second
     */
    private final int tokensPerSecond;

    /**
     * tokens
     */
    private final AtomicInteger tokens;

    /**
     * the threadPool for add tokens task
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TokenBucketLimiter(int capacity, int tokensPerSecond) {
        this.capacity = capacity;
        this.tokensPerSecond = tokensPerSecond;
        this.tokens = new AtomicInteger(capacity);
        startTokenRefillTask();
    }

    public boolean tryAcquire() {
        return tokens.getAndDecrement() > 0;
    }

    private void startTokenRefillTask() {
        scheduler.scheduleAtFixedRate(() -> {
            int currentTokens = tokens.get();
            int newTokens = Math.min(capacity, currentTokens + tokensPerSecond);
            tokens.compareAndSet(currentTokens, newTokens);
        }, 0, 1, TimeUnit.SECONDS);
    }
}
