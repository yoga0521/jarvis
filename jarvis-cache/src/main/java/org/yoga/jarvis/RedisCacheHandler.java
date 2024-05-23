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

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @Description: Cache Handler implemented by redis
 * @Author: yoga
 * @Date: 2024/5/9 16:41
 */
public class RedisCacheHandler<K, V> extends AbstractCacheHandler<K, V> {

    /**
     * redis client
     */
    private final RedisClient redisClient;

    /**
     * commands
     */
    private final RedisCommands<String, String> commands;

    /**
     * expiredTime, ms
     */
    private final long expiredTime;

    public RedisCacheHandler(String host, int port, long expiredTime) {
        this.redisClient = RedisClient.create("redis://" + host + ":" + port);
        this.commands = redisClient.connect().sync();
        this.expiredTime = expiredTime;
    }

    @Override
    public void put(K k, V v) {
        super.put(k, v);
        String serializedVal = serialize(v);
        commands.setex(k.toString(), expiredTime, serializedVal);
    }

    @Override
    public V get(@NotNull K k, @NotNull Function<? super K, ? extends V> mappingFunction) {
        String value = commands.get(k.toString());
        if (value != null) {
            return deserialize(value);
        }
        V newVal = mappingFunction.apply(k);
        if (newVal != null) {
            super.put(k, newVal);
            String serializedVal = serialize(newVal);
            commands.setex(k.toString(), expiredTime, serializedVal);
            return newVal;
        }
        return null;
    }

    @Override
    public V getIfPresent(K k) {
        String value = commands.get(k.toString());
        if (value != null) {
            return deserialize(value);
        }
        return null;
    }

    @Override
    public void remove(K k) {
        commands.del(k.toString());
    }

    @Override
    public void clear() {
        commands.flushall();
    }

    @Override
    public long size() {
        return 0;
    }

    private String serialize(V value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private V deserialize(String value) {
        if (value == null) {
            return null;
        }
        return (V) value;
    }
}
