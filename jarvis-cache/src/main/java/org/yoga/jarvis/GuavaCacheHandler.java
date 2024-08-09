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
import org.yoga.jarvis.exception.JarvisException;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description: Cache Handler implemented by guava
 * @Author: yoga
 * @Date: 2022/5/31 17:12
 */
public class GuavaCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

	private final Cache<K, V> cache;

	public GuavaCacheHandler(int initialCapacity, int maximumSize, long expireIntervalSeconds) {
		this.cache = CacheBuilder.newBuilder()
				// Set the concurrency level is the same as the number of cpus
				// the concurrency level refers to the number of threads
				// that can write to the cache at the same time
				.concurrencyLevel(Runtime.getRuntime().availableProcessors())
				// Set the initial capacity of the cache container to {@code initialCapacity}
				.initialCapacity(initialCapacity)
				// Set the maximum cache capacity to 1000
				// After more than 100, the cache items will be removed according to the LRU algorithm
				// that is rarely used recently.
				.maximumSize(maximumSize)
				// expire after setting write cache
				.expireAfterWrite(expireIntervalSeconds, TimeUnit.SECONDS)
				// Set the hit rate of the cache to be counted
				.recordStats()
				// Set cache removal notifications
				.removalListener((RemovalListener<K, V>) notification -> System.out.println(notification.getKey() +
						" has removed, val: " + notification.getValue() + ", reason: " + notification.getCause()))
				.build();
	}

	@Override
	public void put(K k, V v) {
		super.put(k, v);
		cache.put(k, v);
	}

	@Override
	public V get(K k, Function<? super K, ? extends V> mappingFunction) {
        try {
            return cache.get(k, () -> mappingFunction.apply(k));
        } catch (ExecutionException e) {
            throw new JarvisException(e);
        }
    }

	@Override
	public V getIfPresent(K k) {
		return cache.getIfPresent(k);
	}

	@Override
	public void remove(K k) {
		cache.invalidate(k);
	}

	@Override
	public void clear() {
		cache.invalidateAll();
	}

	@Override
	public long size() {
		cache.cleanUp();
		return cache.size();
	}

	@Override
	public Set<K> keys() {
		return cache.asMap().keySet();
	}
}
