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

/**
 * @Description: SlidingWindow Limiter
 * @Author: yoga
 * @Date: 2022/6/2 11:31
 */
public class SlidingWindowLimiter {

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindowLimiter.class);

    // fixed time window size, ms
    private long windowSize;

    // fixed the number of small windows for window splitting
    private int windowNum;

    // the maximum number of requests allowed to pass through each window
    private int maxRequestCount;

    // count of requests within each window
    private int[] perWindowCount;

    // total count
    private int totalCount;

    // current window index
    private int currentWindowIndex;

    // the size of each small window, ms
    private long perWindowSize;

    // window right border
    private long windowRightBorder;

    public SlidingWindowLimiter(long windowSize, int windowNum, int maxRequestCount) {
        this.windowSize = windowSize;
        this.windowNum = windowNum;
        this.maxRequestCount = maxRequestCount;
        this.perWindowCount = new int[windowNum];
        this.perWindowSize = windowSize / windowNum;
        this.windowRightBorder = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > windowRightBorder) {
            do {
                currentWindowIndex = (++currentWindowIndex) % windowNum;
                totalCount -= perWindowCount[currentWindowIndex];
                perWindowCount[currentWindowIndex] = 0;
                windowRightBorder += perWindowSize;
            } while (windowRightBorder < currentTime);
        }
        if (totalCount < maxRequestCount) {
            logger.info("tryAcquire success,{}", currentWindowIndex);
            perWindowCount[currentWindowIndex]++;
            totalCount++;
            return true;
        } else {
            logger.error("tryAcquire fail,{}", currentWindowIndex);
            return false;
        }
    }
}
