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

package org.yoga.jarvis.spring.boot.autoconfigure.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

/**
 * @Description: the properties object of cache
 * @Author: yoga
 * @Date: 2023/2/15 16:31
 */
@ConfigurationProperties(prefix = "jarvis.cache")
public class CacheProperties {

	/**
	 * initial capacity of cache
	 */
	private int initialCapacity = 64;

	/**
	 * maximum of cache size
	 */
	private int maximumSize = 1024;

	/**
	 * expire interval seconds of cache
	 */
	private int expireIntervalSeconds = 24 * 60 * 60;

	public int getInitialCapacity() {
		return initialCapacity;
	}

	public CacheProperties setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
		return this;
	}

	public int getMaximumSize() {
		return maximumSize;
	}

	public CacheProperties setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
		return this;
	}

	public int getExpireIntervalSeconds() {
		return expireIntervalSeconds;
	}

	public CacheProperties setExpireIntervalSeconds(int expireIntervalSeconds) {
		this.expireIntervalSeconds = expireIntervalSeconds;
		return this;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", CacheProperties.class.getSimpleName() + "[", "]")
				.add("initialCapacity=" + initialCapacity)
				.add("maximumSize=" + maximumSize)
				.add("expireIntervalSeconds=" + expireIntervalSeconds)
				.toString();
	}
}
