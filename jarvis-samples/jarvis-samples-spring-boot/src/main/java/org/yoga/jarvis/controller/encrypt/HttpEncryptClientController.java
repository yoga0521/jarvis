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

package org.yoga.jarvis.controller.encrypt;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.yoga.jarvis.bean.Pair;
import org.yoga.jarvis.util.AESUtils;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.JsonUtils;
import org.yoga.jarvis.util.RSAUtils;
import org.yoga.jarvis.vo.OpenRequestBaseParam;

import java.util.TreeMap;
import java.util.UUID;

/**
 * @Description: http加密客户端controller
 * @Author: yoga
 * @Date: 2022/8/18 10:48
 */
@Slf4j
@RestController
@RequestMapping("encrypt/client")
public class HttpEncryptClientController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${http.client.aes-key}")
    private String aesKey;

    @Value("${http.client.aes-iv}")
    private String aesIv;

    @Value("${http.server.rsa-pub-key}")
    private String serverRsaPubKey;

    @Value("${http.client.rsa-pri-key}")
    private String clientRsaPriKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpEncryptServerController httpEncryptServerController;

    @GetMapping("testReq")
    public String testHttpRequest(String str) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        OpenRequestBaseParam param = buildHttpRequestParam(str);
//        log.info("请求参数: {}", paramJsonStr);
//        HttpEntity<String> request = new HttpEntity<>(paramJsonStr, headers);
//        try {
//            ResponseEntity<Map<String, Object>> response = restTemplate.exchange("http://127.0.0.1:8088/jarvis/boot/encrypt/server/acceptHttpRequest",
//                    HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {
//                    });
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                log.info("返回值: {}", JsonUtils.toJsonStr(response.getBody()));
//            }
//        } catch (Exception e) {
//            log.error("接口调用失败", e);
//        }
        return httpEncryptServerController.acceptHttpRequest(param);
    }

    /**
     * 构建http请求参数json字符串
     *
     * @param data 业务参数
     * @param <T>  泛型
     * @return http请求参数json字符串
     */
    private <T> OpenRequestBaseParam buildHttpRequestParam(T data) {
        Assert.notNull(data, "业务参数为空");

        // 业务参数进行AES加密
        String encryptedParamStr = AESUtils.encryptByCbc(JsonUtils.toJsonStr(data), aesKey, aesIv);
        // AES加密key和向量进行RSA加密（使用服务端的公钥加密，这样只有拥有对应私钥的服务端可以解密）
        String aesKiEncryptedStr = RSAUtils.encryptByPublicKey(aesKey + "$" + aesIv, serverRsaPubKey);

        // 构建请求对象
        OpenRequestBaseParam param = new OpenRequestBaseParam();
        param.setAppName(appName);
        param.setRequestId(UUID.randomUUID().toString());
        param.setTimestamp(System.currentTimeMillis());
        param.setBody(encryptedParamStr);
        param.setBodyEncryptInfo(aesKiEncryptedStr);

        // 转为treeMap排序，再计算签名
        TreeMap<String, Object> treeMap = JsonUtils.deepCopy(param, new TypeReference<TreeMap<String, Object>>() {
        });
        // 移除掉sign字段
        treeMap.remove("sign");
        // 设置签名（使用客户端的私钥签名，这样只有拥有私钥的客户端可以向服务端发送有效请求，其他的请求验签都会失败）
        param.setSign(RSAUtils.sign(JsonUtils.toJsonStr(treeMap), clientRsaPriKey));
        return param;
    }

    public static void main(String[] args) {
        Pair<String, String> pair = RSAUtils.generateKeysPairStr();
        System.out.println("priKey:" + pair.getLeft());
        System.out.println("pubKey:" + pair.getRight());
    }
}
