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

package org.yoga.jarvis.listener;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.yoga.jarvis.cache.ApplicationRouteRuleCache;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.core.ApplicationRouteRule;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.factory.ThreadFactoryBuilder;
import org.yoga.jarvis.task.SyncRegisteredAppTask;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.CollectionUtils;
import org.yoga.jarvis.util.JsonUtils;
import org.yoga.jarvis.util.NetUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: Sync Registered App Task Listener
 * @Author: yoga
 * @Date: 2024/7/4 16:45
 */
public class SyncRegisteredAppTaskListener implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Logger logger = LoggerFactory.getLogger(SyncRegisteredAppTaskListener.class);

    private static final String NACOS_DATA_ID = "app-config";

    private static final ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder().setNameFormat("sync-registered-app").build());

    @NacosInjected
    private NamingService namingService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        ServerConfigs serverConfigs = new ServerConfigs();
        logger.info("server refresh interval: {}s", serverConfigs.getCacheRefreshInterval());
        scheduledPool.scheduleWithFixedDelay(new SyncRegisteredAppTask(namingService), 0L,
                serverConfigs.getCacheRefreshInterval(), TimeUnit.MICROSECONDS);
        // register nacos
        InetAddress inetAddress = NetUtils.getLocalIpAddress0();
        Assert.notNull(inetAddress, "jarvis-server inetAddress is null");
        String serverPort = event.getApplicationContext().getEnvironment().getProperty("server.port");
        Assert.notBlank(serverPort, "jarvis-server serverPort is null");
        try {
            namingService.registerInstance("jarvis-server", SyncRegisteredAppTask.APP_GROUP_NAME, inetAddress.getHostAddress(), Integer.parseInt(serverPort));
        } catch (NacosException e) {
            throw new JarvisException("jarvis-server register nacos failed", e);
        }
        // config
        try {
            String nacosServerAddr = event.getApplicationContext().getEnvironment().getProperty("nacos.discovery.server-addr");
            Assert.notBlank(nacosServerAddr, "nacos discovery server-addr must not be null");
            ConfigService configService = NacosFactory.createConfigService(nacosServerAddr);
            // get config from nacos
            String configInfo = configService.getConfig(NACOS_DATA_ID, SyncRegisteredAppTask.APP_GROUP_NAME, 5000);
            // init config
            updateConfig(configInfo);
            // add nacos config listener
            configService.addListener(NACOS_DATA_ID, SyncRegisteredAppTask.APP_GROUP_NAME, new Listener() {

                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    logger.info("receive configs from nacos: {}", configInfo);
                    // update config
                    updateConfig(configInfo);
                }
            });
        } catch (NacosException e) {
            throw new JarvisException("get configs from nacos failed", e);
        }
    }

    /**
     * update nacos config
     *
     * @param configInfo config info
     */
    private void updateConfig(String configInfo) {
        List<ApplicationRouteRule> applicationRouteRules = JsonUtils.parseObj(configInfo, new TypeReference<List<ApplicationRouteRule>>() {
        });
        Map<String, List<ApplicationRouteRule>> applicationRouteRuleMap = CollectionUtils.isEmpty(applicationRouteRules)
                ? Maps.newHashMap() : applicationRouteRules.stream().collect(Collectors.groupingBy(ApplicationRouteRule::getApplicationName));
        ApplicationRouteRuleCache.addAll(applicationRouteRuleMap);
        logger.info("update application route rule config success");
    }
}
