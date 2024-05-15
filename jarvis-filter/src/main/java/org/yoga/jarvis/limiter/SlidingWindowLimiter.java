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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Description: SlidingWindow Limiter
 * @Author: yoga
 * @Date: 2022/6/2 11:31
 */
public class SlidingWindowLimiter {

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindowLimiter.class);

    // window size
    private final long windowSize;

    // max request per window
    private final int maxRequestPerWindow;

    // queue
    private final Queue<Long> queue = new ConcurrentLinkedQueue<>();

    public SlidingWindowLimiter(long windowSize, int maxRequestPerWindow) {
        this.windowSize = windowSize;
        this.maxRequestPerWindow = maxRequestPerWindow;
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowSize;
        // clean expired timestamp
        while (!queue.isEmpty() && queue.peek() < windowStart) {
            queue.poll();
        }

        if (queue.size() < maxRequestPerWindow) {
            queue.add(currentTime);
            return true;
        }
        return false;
    }
}
