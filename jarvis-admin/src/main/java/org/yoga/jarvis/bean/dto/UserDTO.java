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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 用户dto
 * @Author: yoga
 * @Date: 2024/8/26 10:04
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1443282224668822529L;

    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名为空")
    @Length(min = 1, max = 32, message = "用户名长度不能超过32")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    private String password;
}
