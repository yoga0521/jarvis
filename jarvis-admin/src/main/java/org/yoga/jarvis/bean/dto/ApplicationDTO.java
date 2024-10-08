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
 * @Description: 应用信息dto
 * @Author: yoga
 * @Date: 2024/9/2 16:13
 */
@Data
public class ApplicationDTO implements Serializable {

    private static final long serialVersionUID = -1322896682867192700L;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 是否开启，0禁用 1开启
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;
}
