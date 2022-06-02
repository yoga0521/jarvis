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

import org.yoga.jarvis.util.Assert;

/**
 * @Description: Abstract CacheHandler
 * @Author: yoga
 * @Date: 2022/6/2 13:43
 */
public abstract class AbstractCacheHandler<K, V> implements CacheHandler<K, V> {

    /**
     * Default cache effective time(ms)
     * 1 hours
     */
    protected static final long DEFAULT_EFFECTIVE_TIME = 60 * 60 * 60 * 1000L;

    /**
     * Default cache size
     */
    protected static final int DEFAULT_SIZE = 1000;

    @Override
    public void add(K k, V v) {
        Assert.notNull(k, "key must not be null!");
        Assert.notNull(v, "value must not be null!");
    }

    @Override
    public void add(K k, V v, long effectiveTime) {
        Assert.notNull(k, "key must not be null!");
        Assert.notNull(v, "value must not be null!");
        Assert.isTrue(effectiveTime > 0, "effectiveTime must > 0!");
    }

    /**
     * If there is no cache, get it through this method
     *
     * @param k key
     * @return getIfKNotExist value
     */
    protected V getIfNotExist(K k) {
        return null;
    }

}
