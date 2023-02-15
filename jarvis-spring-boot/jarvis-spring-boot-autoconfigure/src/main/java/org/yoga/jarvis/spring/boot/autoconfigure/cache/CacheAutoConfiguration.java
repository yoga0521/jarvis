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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yoga.jarvis.AbstractCacheHandler;
import org.yoga.jarvis.CacheHandler;
import org.yoga.jarvis.CaffeineCacheHandler;
import org.yoga.jarvis.GuavaCacheHandler;

/**
 * @Description: cache auto configure
 * @Author: yoga
 * @Date: 2023/2/15 16:31
 */
@Configuration
@ConditionalOnClass({CacheHandler.class, AbstractCacheHandler.class, GuavaCacheHandler.class, CaffeineCacheHandler.class})
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

	private final CacheProperties cacheProperties;

	public CacheAutoConfiguration(CacheProperties cacheProperties) {
		this.cacheProperties = cacheProperties;
	}

	/**
	 * create GuavaCacheHandler Bean
	 *
	 * @return GuavaCacheHandler {@link org.yoga.jarvis.GuavaCacheHandler}
	 */
	@Bean("guavaCacheHandler")
	@ConditionalOnMissingBean(GuavaCacheHandler.class)
	GuavaCacheHandler guavaCacheHandler() {
		return new GuavaCacheHandler(cacheProperties.getInitialCapacity(), cacheProperties.getMaximumSize(), cacheProperties.getExpireIntervalSeconds());
	}

	/**
	 * create CaffeineCacheHandler Bean
	 *
	 * @return CaffeineCacheHandler {@link org.yoga.jarvis.CaffeineCacheHandler}
	 */
	@Bean("caffeineCacheHandler")
	@ConditionalOnMissingBean(CaffeineCacheHandler.class)
	CaffeineCacheHandler caffeineCacheHandler() {
		return new CaffeineCacheHandler(cacheProperties.getInitialCapacity(), cacheProperties.getMaximumSize(), cacheProperties.getExpireIntervalSeconds());
	}

}
