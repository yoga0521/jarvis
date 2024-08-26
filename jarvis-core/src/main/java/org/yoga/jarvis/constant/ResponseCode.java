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

package org.yoga.jarvis.constant;

/**
 * @Description: Response Code
 * @Author: yoga
 * @Date: 2024/8/26 10:49
 */
public enum ResponseCode {

    /**
     * success
     */
    SUCCESS("200", "success", "请求成功"),

    /**
     * param validate failed
     */
    VALIDATE_FAILED("400", "param validate failed", "参数校验失败"),

    /**
     * not login
     */
    USER_NOT_LOGIN("401", "not login", "未登陆"),

    /**
     * system error
     */
    ERROR("500", "system error", "系统异常");

    /**
     * 错误code
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 错误信息
     */
    private final String zhCnMessage;

    ResponseCode(String code, String message, String zhCnMessage) {
        this.code = code;
        this.message = message;
        this.zhCnMessage = zhCnMessage;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getZhCnMessage() {
        return zhCnMessage;
    }
}
