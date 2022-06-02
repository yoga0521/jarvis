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

package org.yoga.jarvis;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Cache Handler implemented by guava
 * @Author: yoga
 * @Date: 2022/5/31 17:12
 */
public class GuavaCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

    Cache<K, V> cache = CacheBuilder.newBuilder()
            // Set the concurrency level is the same as the number of cpus
            // the concurrency level refers to the number of threads
            // that can write to the cache at the same time
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            // 1 hours refresh after setting write cache
            .refreshAfterWrite(1, TimeUnit.HOURS)
            // Set the initial capacity of the cache container to 1000
            .initialCapacity(1000)
            // Set the maximum cache capacity to 1000
            // After more than 100, the cache items will be removed according to the LRU algorithm
            // that is rarely used recently.
            .maximumSize(1000)
            // Set the hit rate of the cache to be counted
            .recordStats()
            // Set cache removal notifications
            .removalListener((RemovalListener<K, V>) notification ->
                    System.out.println(notification.getKey() + " has removed, reason: " + notification.getCause()))
            // CacheLoader todo
            .build();

    @Override
    public void add(K k, V v) {
        super.add(k, v);
        cache.put(k, v);
    }

    @Override
    public V get(K k) throws ExecutionException {
        return cache.get(k, () -> getIfNotExist(k));
    }

    @Override
    public V remove(K k) {

        cache.invalidate(k);
        return null;
    }

    @Override
    public void clear() {
        if (size() > 0) {
            cache.invalidateAll();
        }
    }

    @Override
    public long size() {
        return cache.size();
    }
}
