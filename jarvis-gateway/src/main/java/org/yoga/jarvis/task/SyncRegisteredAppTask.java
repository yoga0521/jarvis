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

package org.yoga.jarvis.task;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoga.jarvis.cache.ServerInstanceCache;
import org.yoga.jarvis.constant.CommonConstant;
import org.yoga.jarvis.core.ServerInstance;
import org.yoga.jarvis.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Sync Registered App Task
 * @Author: yoga
 * @Date: 2024/7/2 10:58
 */
public class SyncRegisteredAppTask implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(SyncRegisteredAppTask.class);

    private final NamingService namingService;

    public SyncRegisteredAppTask(NamingService namingService) {
        this.namingService = namingService;
    }

    @Override
    public void run() {
        try {
            ListView<String> servers = namingService.getServicesOfServer(1, Integer.MAX_VALUE, CommonConstant.APP_GROUP_NAME);
            if (servers == null || CollectionUtils.isEmpty(servers.getData())) {
                return;
            }
            List<String> appNames = servers.getData();
            for (String appName : appNames) {
                List<Instance> instances = namingService.getAllInstances(appName, CommonConstant.APP_GROUP_NAME);
                if (CollectionUtils.isEmpty(instances)) {
                    continue;
                }
                ServerInstanceCache.add(appName, instances.stream()
                        .map(instance -> {
                            ServerInstance serverInstance = new ServerInstance();
                            serverInstance.setIp(instance.getIp());
                            serverInstance.setPort(instance.getPort());
                            serverInstance.setVersion(CollectionUtils.isEmpty(instance.getMetadata())
                                    ? CommonConstant.DEFAULT_APP_VERSION : instance.getMetadata().getOrDefault("version", CommonConstant.DEFAULT_APP_VERSION));
                            return serverInstance;
                        })
                        .collect(Collectors.toList()));
            }

        } catch (NacosException e) {
            logger.error("sync registered apps from nacos error!", e);
        }
    }
}
