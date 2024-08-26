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

package org.yoga.jarvis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yoga.jarvis.bean.constant.CommonConstant;
import org.yoga.jarvis.repository.entity.UserDO;
import org.yoga.jarvis.repository.mapper.UserMapper;
import org.yoga.jarvis.util.Assert;

import java.time.LocalDateTime;

/**
 * @Description: 用户service
 * @Author: yoga
 * @Date: 2024/8/23 13:34
 */
@Slf4j
@Service
public class UserService {

    @Value("${user.login.salt}")
    private String userLoginSalt;

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 登录
     *
     * @param name     用户名
     * @param password 密码
     */
    public void login(String name, String password) {
        Assert.notBlank(name, "用户名为空");
        Assert.notBlank(password, "密码为空");
        QueryWrapper<UserDO> qw = new QueryWrapper<>();
        qw.eq(UserDO.NAME, name);
        qw.eq(UserDO.PASSWORD, DigestUtils.md5Hex(password + userLoginSalt));
        qw.eq(UserDO.IS_DELETE, CommonConstant.IS_DEL_NOT);
        UserDO user = userMapper.selectOne(qw);
        Assert.notNull(user, "用户名或密码错误");
    }

    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     */
    public void register(String name, String password) {
        Assert.notBlank(name, "用户名为空");
        Assert.notBlank(password, "密码为空");
        QueryWrapper<UserDO> qw = new QueryWrapper<>();
        qw.eq(UserDO.NAME, name);
        qw.eq(UserDO.IS_DELETE, CommonConstant.IS_DEL_NOT);
        Assert.isTrue(userMapper.selectCount(qw) == 0, "用户名已存在");
        UserDO user = new UserDO();
        user.setName(name);
        user.setPassword(DigestUtils.md5Hex(password + userLoginSalt));
        user.setGmtCreated(LocalDateTime.now());
        userMapper.insert(user);
    }
}
