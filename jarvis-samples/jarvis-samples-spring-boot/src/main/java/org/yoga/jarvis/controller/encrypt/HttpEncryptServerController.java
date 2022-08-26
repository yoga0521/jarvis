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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.JsonUtils;
import org.yoga.jarvis.util.crypto.AESUtils;
import org.yoga.jarvis.util.crypto.RSAUtils;
import org.yoga.jarvis.vo.OpenRequestBaseParam;

import java.util.TreeMap;

/**
 * @Description: http加密服务端controller
 * @Author: yoga
 * @Date: 2022/8/18 10:48
 */
@Slf4j
@Validated
@RestController
@RequestMapping("encrypt/server")
public class HttpEncryptServerController {

    @Value("${http.server.rsa-pri-key}")
    private String serverPriKey;

    @Value("${http.client.rsa-pub-key}")
    private String clientPubKey;

    @PostMapping("acceptHttpRequest")
    public String acceptHttpRequest(@RequestBody OpenRequestBaseParam param) {
        return resolveRequest(param, String.class);
    }

    /**
     * 核验解密请求参数
     *
     * @param param     请求参数
     * @param valueType 反序列化的类型
     * @param <T>       泛型
     * @return 反序列化的对象
     */
    private <T> T resolveRequest(OpenRequestBaseParam param, Class<T> valueType) {
        // 时间戳大于10秒则视为请求失效
        if (System.currentTimeMillis() - param.getTimestamp() > 10 * 1000L) {
            throw new JarvisException("请求失效");
        }
        // 转为treeMap排序，再验签
        TreeMap<String, Object> treeMap = JsonUtils.deepCopy(param, new TypeReference<TreeMap<String, Object>>() {
        });
        // 移除掉sign字段
        treeMap.remove("sign");

        // RSA验签（使用客户端的公钥进行验签，保证只接收客户端的消息）
        if (!RSAUtils.verify(JsonUtils.toJsonStr(treeMap), clientPubKey, param.getSign())) {
            throw new JarvisException("验签失败");
        }

        String encryptInfoStr = RSAUtils.decryptByPrivateKey(param.getBodyEncryptInfo(), serverPriKey);
        String[] arr = encryptInfoStr.split("\\$");
        if (null == arr || arr.length != 2) {
            throw new JarvisException("密钥信息格式错误");
        }
        // 0=aesKey, 1=aesIv
        String dataJsonStr = AESUtils.decryptByCbc(param.getBody(), arr[0], arr[1]);
        // 转成参数
        return JsonUtils.parseObj(dataJsonStr, valueType);
    }
}
