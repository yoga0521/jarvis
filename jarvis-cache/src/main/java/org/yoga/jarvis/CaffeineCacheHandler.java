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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Caffeine Cache Handler
 * @Author: yoga
 * @Date: 2022/6/1 18:13
 */
public class CaffeineCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

    Cache<K, V> cache = Caffeine.newBuilder()
            // Set the initial capacity of the cache container to 64
            .initialCapacity(64)
            // Set the maximum cache capacity to 100
            // After more than 100, the cache items will be removed according to the LRU algorithm
            // that is rarely used recently.
            .maximumSize(100)
            // 60 second refresh after setting write cache
            .expireAfterWrite(60, TimeUnit.SECONDS)
            // Set the hit rate of the cache to be counted
            .recordStats()
            // Set cache removal notifications
            .removalListener((RemovalListener<K, V>) (k, v, removalCause) -> System.out.println(k + " has removed, reason: " + v))
            .build();

    @Override
    public void add(K k, V v, long effectiveTime) {
        cache.put(k, v);
    }

    @Override
    public V get(K k) throws ExecutionException {
        return cache.get(k, super::getIfNotExist);
    }

    @Override
    public V remove(K k) {
        cache.invalidate(k);
        return null;
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    @Override
    public long size() {
        cache.cleanUp();
        return cache.estimatedSize();
    }
}
