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

import org.yoga.jarvis.factory.ThreadFactoryBuilder;
import org.yoga.jarvis.util.Assert;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Default Cache Handler
 * @Author: yoga
 * @Date: 2022/5/28 21:06
 */
public class DefaultCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

    /**
     * cache
     */
    private final Map<K, Item> cache = new ConcurrentHashMap<>();

    /**
     * Used to scan and remove expired caches
     */
    private final ExecutorService executor = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNameFormat("cache-pool").setDaemon(true).build());

    public DefaultCacheHandler() {
        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(20 * 60 * 1000L);
                    cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

    }

    @Override
    public void add(K k, V v) {
        super.add(k, v);
        cache.put(k, new Item(v, System.currentTimeMillis() + DEFAULT_EFFECTIVE_TIME));
    }

    @Override
    public void add(K k, V v, long effectiveTime) {
        super.add(k, v, effectiveTime);
        cache.put(k, new Item(v, System.currentTimeMillis() + effectiveTime));
    }

    @Override
    public V get(K k) {
        Assert.notNull(k, "key must not be null!");
        return cache.get(k).getV();
    }

    @Override
    public V remove(K k) {
        Assert.notNull(k, "key must not be null!");
        return cache.remove(k).getV();
    }

    @Override
    public void clear() {
        if (size() > 0) {
            cache.clear();
        }
    }

    @Override
    public long size() {
        return cache.size();
    }

    /**
     * the item for cache
     */
    final class Item implements Serializable {

        private static final long serialVersionUID = 5522441429013497696L;

        /**
         * the cache value
         */
        private final V v;

        /**
         * the cache expireTime
         */
        private long expireTime;

        public Item(final V v, long expireTime) {
            this.v = v;
            this.expireTime = expireTime;
        }

        public V getV() {
            return v;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > this.expireTime;
        }

    }
}
