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

package org.yoga.jarvis.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 开放接口请求基础参数对象
 * @Author: yoga
 * @Date: 2022/8/18 11:25
 */
@Data
public class OpenRequestBaseParam implements Serializable {

    private static final long serialVersionUID = -829293625261082664L;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名为空")
    private String appName;

    /**
     * 请求id，用于追踪请求
     */
    @NotBlank(message = "请求id为空")
    private String requestId;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 业务参数（加密后）
     */
    @NotBlank(message = "业务参数为空")
    private String body;

    /**
     * 业务参数加密的信息（如aes加密信息）
     */
    @NotBlank(message = "业务参数加密信息为空")
    private String bodyEncryptInfo;

    /**
     * 验签参数
     */
    @NotBlank(message = "参数签名为空")
    private String sign;
}
