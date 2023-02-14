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

import javax.annotation.Nullable;

/**
 * @Description: Cache Handler
 * @Author: yoga
 * @Date: 2022/5/28 20:49
 */
public interface CacheHandler<K, V> {

	/**
	 * add cache
	 *
	 * @param k key, not null
	 * @param v value, not null
	 */
	void put(K k, V v);

	/**
	 * get cache
	 *
	 * @param k key, not null
	 * @return value
	 */
	@Nullable
	V getIfPresent(K k);

	/**
	 * remove cache
	 *
	 * @param k key
	 */
	void remove(K k);

	/**
	 * clear all cache
	 */
	void clear();

	long size();

}
