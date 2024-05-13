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
 * @Description: Leaky Bucket Limiter
 * @Author: yoga
 * @Date: 2024/5/13 15:44
 */
public class LeakyBucketLimiter {

    private static final Logger logger = LoggerFactory.getLogger(LeakyBucketLimiter.class);

    /**
     * capacity
     */
    private final int capacity;

    /**
     * leak rate
     */
    private final int leakRate;

    /**
     * water level, the current water level
     */
    private final AtomicInteger waterLevel;

    /**
     * the threadPool for leak task
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LeakyBucketLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.waterLevel = new AtomicInteger(0);
        startLeakTask();
    }

    public boolean tryAcquire() {
        return waterLevel.getAndIncrement() < capacity;
    }

    private void startLeakTask() {
        scheduler.scheduleAtFixedRate(() -> {
            waterLevel.set(Math.max(0, waterLevel.get() - leakRate));
        }, 0, 1, TimeUnit.SECONDS);
    }
}
