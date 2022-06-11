/*
 *  Copyright 2022 yoga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.yoga.jarvis.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: SimpleWindow Limiter
 * @Author: yoga
 * @Date: 2022/6/2 13:24
 */
public class SimpleWindowLimiter {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWindowLimiter.class);

    /**
     * threshold
     */

    private static Integer threshold = 2;

    /**
     * time windows（ms）
     */
    private static long timeWindows = 1000;
    /**
     * counter
     */
    private static AtomicInteger counter = new AtomicInteger();

    public synchronized static boolean tryAcquire(long timeout, TimeUnit unit) {
        return counter.incrementAndGet() <= threshold;
    }
}
