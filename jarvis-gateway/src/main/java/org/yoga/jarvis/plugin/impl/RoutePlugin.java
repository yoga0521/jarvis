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

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.yoga.jarvis.cache.ServerInstanceCache;
import org.yoga.jarvis.config.ServerConfigs;
import org.yoga.jarvis.core.ServerInstance;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.plugin.Plugin;
import org.yoga.jarvis.chain.PluginChain;
import org.yoga.jarvis.spi.LoadBalance;
import org.yoga.jarvis.spi.balance.LoadBalanceFactory;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description: Route Plugin
 * @Author: yoga
 * @Date: 2024/6/18 15:30
 */
public class RoutePlugin implements Plugin {

    protected static final Logger logger = LoggerFactory.getLogger(RoutePlugin.class);

    /**
     * Gateway Server Configs
     */
    private final ServerConfigs serverConfigs;

    /**
     * server error
     */
    private static final String SERVER_ERROR_RESULT = "{\"code\":500,\"message\":\"system error\"}";

    /**
     * gateway timeout
     */
    private static final String GATEWAY_TIMEOUT_RESULT = "{\"code\":504,\"message\":\"network timeout\"}";

    /**
     * Use reactive web client
     */
    private static final WebClient webClient;

    static {
        webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10))
                                .addHandlerLast(new WriteTimeoutHandler(10)))))
                .build();
    }

    public RoutePlugin(ServerConfigs serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    @Override
    public Integer order() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public String name() {
        return "Route";
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        ServerHttpRequest request = exchange.getRequest();
        String appName = pluginChain.getAppName();
        //
        ServerInstance serverInstance = chooseInstance(pluginChain.getAppName(), request);
        Assert.notNull(serverInstance, appName + " no server available!");
        // build new url, http://ip:port/path
        String url = "http://" + serverInstance.getIp() + ":" + serverInstance.getPort() +
                request.getPath().value().replaceFirst("/" + appName, "");
        if (StringUtils.isNotBlank(request.getURI().getQuery())) {
            url += "?" + request.getURI().getQuery();
        }
        return forward(exchange, url);
    }

    /**
     * forward http request
     *
     * @param exchange ServerWebExchange
     * @param url      http request url
     * @return result
     */
    private Mono<Void> forward(ServerWebExchange exchange, String url) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpMethod method = request.getMethod();
        Assert.notNull(method, "unknown http method");

        WebClient.RequestBodySpec requestBodySpec = webClient.method(method).uri(url).headers(headers -> headers.addAll(request.getHeaders()));
        WebClient.RequestHeadersSpec<?> reqHeadersSpec;
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)) {
            reqHeadersSpec = requestBodySpec.body(BodyInserters.fromDataBuffers(request.getBody()));
        } else {
            reqHeadersSpec = requestBodySpec;
        }

        return reqHeadersSpec
                .exchangeToMono(resp -> {
                    response.setStatusCode(resp.statusCode());
                    response.getHeaders().putAll(resp.headers().asHttpHeaders());
                    return response.writeWith(resp.bodyToFlux(DataBuffer.class));
                })
                .timeout(Duration.ofMillis(serverConfigs.getTimeout()))
                .onErrorResume(ex -> Mono
                        .defer(() -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
                                (ex instanceof TimeoutException ? GATEWAY_TIMEOUT_RESULT : SERVER_ERROR_RESULT).getBytes()))))
                        .then(Mono.empty()));
    }

    /**
     * choose server instance
     *
     * @param appName app name
     * @param request http request
     * @return server instance
     */
    private ServerInstance chooseInstance(String appName, ServerHttpRequest request) {
        List<ServerInstance> serverInstances = ServerInstanceCache.getServerInstances(appName);
        if (CollectionUtils.isEmpty(serverInstances)) {
            logger.error("server instance[{}] not find", appName);
            throw new JarvisException("server instance not find");
        }
        // version todo
        String version = "v1";
        //Select an instance based on the loadBalance algorithm
        LoadBalance loadBalance = LoadBalanceFactory.getInstance(serverConfigs.getLoadBalance(), appName, version);
        return loadBalance.select(serverInstances);
    }
}
