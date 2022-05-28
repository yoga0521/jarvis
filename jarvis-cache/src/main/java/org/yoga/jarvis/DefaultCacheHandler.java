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

import java.lang.ref.SoftReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Default Cache Handler
 * @Author: yoga
 * @Date: 2022/5/28 21:06
 */
public class DefaultCacheHandler<K, V> implements CacheHandler<K, V> {

    private final ConcurrentHashMap<K, SoftReference<V>> cache = new ConcurrentHashMap<>();

    @Override
    public void add(K k, V v, long effectiveTime) {
        cache.put(k, new SoftReference<>(v));
    }

    @Override
    public V get(K k) {
        return Optional.ofNullable(cache.get(k)).map(SoftReference::get).orElse(null);
    }

    @Override
    public V remove(K k) {
        return Optional.ofNullable(cache.remove(k)).map(SoftReference::get).orElse(null);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

}
