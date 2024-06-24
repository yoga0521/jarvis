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

package org.yoga.jarvis.spi.balance;

import org.yoga.jarvis.CacheHandler;
import org.yoga.jarvis.GuavaCacheHandler;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.spi.LoadBalance;
import org.yoga.jarvis.util.Assert;

import java.util.ServiceLoader;

/**
 * @Description: Load Balance Factory
 * @Author: yoga
 * @Date: 2024/6/21 17:07
 */
public class LoadBalanceFactory {

    private static final CacheHandler<String, LoadBalance> LOAD_BALANCE_CACHE = new GuavaCacheHandler<>(8, 32, 24 * 60 * 60);

    private LoadBalanceFactory() {
    }

    /**
     * get the instance of load balance
     *
     * @param name    load balance name
     * @param appName app name
     * @param version version
     * @return the instance of load balance
     */
    public static LoadBalance getInstance(final String name, String appName, String version) {
        return LOAD_BALANCE_CACHE.get(appName + ":" + version, k -> getLoadBalance(name));
    }

    /**
     * get load balance by name
     *
     * @param name load balance name
     * @return load balance
     */
    private static LoadBalance getLoadBalance(String name) {
        Assert.notBlank(name, "load balance name can not be blank!");
        ServiceLoader<LoadBalance> loader = ServiceLoader.load(LoadBalance.class);
        for (LoadBalance loadBalance : loader) {
            if (name.equals(loadBalance.getName())) {
                return loadBalance;
            }
        }
        throw new JarvisException("invalid load balance name");
    }
}
