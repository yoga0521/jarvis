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

package org.yoga.jarvis.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoga.jarvis.bean.Result;
import org.yoga.jarvis.bean.dto.UserDTO;
import org.yoga.jarvis.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Description: 用户controller
 * @Author: yoga
 * @Date: 2024/8/26 12:01
 */
@Validated
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 注册用户
     *
     * @param param 请求参数
     */
    @PostMapping("register")
    public Result<Void> register(@RequestBody @NotNull(message = "请求参数为空") @Valid UserDTO param) {
        userService.register(param.getName(), param.getPassword());
        return Result.success();
    }
}
