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

package org.yoga.jarvis.plugin.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.yoga.jarvis.chain.PluginChain;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.plugin.Plugin;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.CollectionUtils;
import org.yoga.jarvis.util.NetUtils;
import org.yoga.jarvis.util.PatternUtils;
import org.yoga.jarvis.util.StringUtils;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @Description: Authorization Plugin
 * @Author: yoga
 * @Date: 2024/6/7 15:28
 */
public class AuthorizationPlugin implements Plugin {

    protected static final Logger logger = LoggerFactory.getLogger(AuthorizationPlugin.class);

    private static final String UNKNOWN = "unknown";

    /**
     * Gateway Server Configs
     */
    private final ServerConfigs serverConfigs;

    public AuthorizationPlugin(ServerConfigs serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public Integer order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String name() {
        return "Authorization";
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        logger.info("authorization plugin start execute");
        // check ip whitelist
        if (CollectionUtils.isNotEmpty(serverConfigs.getIpWhitelist())) {
            String realIp = getRealIpAddress(exchange.getRequest());
            Assert.isTrue(PatternUtils.fuzzyMatch(serverConfigs.getIpWhitelist(), realIp), "IP address is not authorized");
        }
        return pluginChain.execute(exchange, pluginChain);
    }

    /**
     * get the real IP address
     *
     * @param request http request
     * @return real IP address
     */
    private static String getRealIpAddress(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        HttpHeaders headers = request.getHeaders();
        String ipAddress = headers.getFirst("X-Forwarded-For");
        if (StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();
                if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                    InetAddress inet = NetUtils.getLocalIpAddress0();
                    if (null != inet) {
                        ipAddress = inet.getHostAddress();
                    }
                }
            }
        }

        // In the case of multiple proxies, the first IP is the real IP of the client
        // and the multiple IPs are split according to ','
        // "***.***.***.***".length() = 15
        if (StringUtils.isNotBlank(ipAddress) && ipAddress.length() > 15) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
}
