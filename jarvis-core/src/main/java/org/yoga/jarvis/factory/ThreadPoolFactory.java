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

package org.yoga.jarvis.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: thread pool factory
 * @Author: yoga
 * @Date: 2022/5/19 16:23
 */
public class ThreadPoolFactory {

    /**
     * Generate a thread pool for I/O operations
     *
     * @param nameFormat thread name format
     * @return thread pool
     */
    public static ExecutorService generateIOThreadPool(String nameFormat) {
        return new ThreadPoolExecutor(
                2 * Runtime.getRuntime().availableProcessors(),
                4 * Runtime.getRuntime().availableProcessors(),
                5000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadFactoryBuilder().setNameFormat(nameFormat).build());
    }

}
