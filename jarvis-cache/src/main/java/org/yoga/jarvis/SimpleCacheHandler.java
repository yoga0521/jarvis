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

package org.yoga.jarvis;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @Description: Simple Cache Handler
 * @Author: yoga
 * @Date: 2024/5/20 16:03
 */
public class SimpleCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

    /**
     * cache map
     */
    private final Map<K, CacheValue<V>> cache = new ConcurrentHashMap<>();

    /**
     * expiredTime, ms
     */
    private final long expiredTime;

    public SimpleCacheHandler(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public void put(K k, V v) {
        super.put(k, v);
        cache.put(k, new CacheValue<>(v, System.currentTimeMillis()));
    }

    @Override
    public V get(@NotNull K k, @NotNull Function<? super K, ? extends V> mappingFunction) {
        CacheValue<V> cacheValue = cache.get(k);
        if (cacheValue != null && (cacheValue.getTimestamp() + expiredTime > System.currentTimeMillis())) {
            return cacheValue.getValue();
        }
        V newVal = mappingFunction.apply(k);
        if (newVal != null) {
            put(k, newVal);
            return newVal;
        }
        return null;
    }

    @Override
    public V getIfPresent(@NotNull K k) {
        CacheValue<V> cacheValue = cache.get(k);
        if (cacheValue != null && (cacheValue.getTimestamp() + expiredTime > System.currentTimeMillis())) {
            return cacheValue.getValue();
        }
        return null;
    }

    @Override
    public void remove(@NotNull K k) {
        cache.remove(k);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

    /**
     * Cache Value
     *
     * @param <V> generics of value
     */
    private static class CacheValue<V> {

        /**
         * value
         */
        private final V value;

        /**
         * create timestamp
         */
        private final long timestamp;

        public CacheValue(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        public V getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

}
