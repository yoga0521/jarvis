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
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.TimeUnit;

/**
 * @Description: Cache Handler implemented by guava
 * @Author: yoga
 * @Date: 2022/5/31 17:12
 */
public class GuavaCacheHandler<K, V> implements CacheHandler<K, V> {

    Cache<K, V> cache = CacheBuilder.newBuilder()
            // Set the concurrency level to 10
            // the concurrency level refers to the number of threads
            // that can write to the cache at the same time
            .concurrencyLevel(10)
            // 1 minute after setting write cache expires
            .expireAfterWrite(60, TimeUnit.SECONDS)
            // 1 second refresh after setting write cache
            .refreshAfterWrite(1, TimeUnit.SECONDS)
            // Set the initial capacity of the cache container to 64
            .initialCapacity(64)
            // Set the maximum cache capacity to 100
            // After more than 100, the cache items will be removed according to the LRU algorithm
            // that is rarely used recently.
            .maximumSize(100)
            // Set the hit rate of the cache to be counted
            .recordStats()
            // Set cache removal notifications
            .removalListener(new RemovalListener<K, V>() {
                @Override
                public void onRemoval(RemovalNotification<K, V> notification) {
                    System.out.println(notification.getKey() + " has removed, reason: " + notification.getCause());
                }
            })
            // CacheLoader todo
            .build();

    @Override
    public void add(K k, V v, long effectiveTime) {
        cache.put(k, v);
    }

    @Override
    public V get(K k) {
        return cache.getIfPresent(k);
    }

    @Override
    public V remove(K k) {
        cache.invalidate(k);
        return null;
    }

    @Override
    public void clear() {
        cache.cleanUp();
    }

    @Override
    public long size() {
        return cache.size();
    }
}
