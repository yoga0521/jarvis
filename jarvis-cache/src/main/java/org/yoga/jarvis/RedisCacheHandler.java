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

import org.jetbrains.annotations.Nullable;

/**
 * @Description: Cache Handler implemented by redis
 * @Author: yoga
 * @Date: 2024/5/9 16:41
 */
public class RedisCacheHandler<K, V> extends AbstractCacheHandler<K, V> {


    @Override
    public V getIfPresent(K k) {
        return null;
    }

    @Override
    public void remove(K k) {

    }

    @Override
    public void clear() {

    }

    @Override
    public long size() {
        return 0;
    }
}
