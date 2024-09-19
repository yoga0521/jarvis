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

package org.yoga.jarvis.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 路由规则dto
 * @Author: yoga
 * @Date: 2024/9/19 13:05
 */
@Data
public class RuleDTO implements Serializable {

    private static final long serialVersionUID = 5900025869764168357L;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 路由版本
     */
    private String version;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 优先级，值越大优先级越高
     */
    private Integer priority;

    /**
     * 匹配key
     */
    private String matchKey;

    /**
     * 匹配方式
     */
    private Boolean matchMethod;

    /**
     * 匹配规则
     */
    private String matchRule;

    /**
     * 是否开启，0禁用 1开启
     */
    private Boolean enabled;
}
