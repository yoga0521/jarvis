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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yoga.jarvis.bean.dto.ApplicationDTO;
import org.yoga.jarvis.repository.entity.ApplicationDO;
import org.yoga.jarvis.repository.mapper.ApplicationMapper;
import org.yoga.jarvis.util.JsonUtils;

import java.time.LocalDateTime;

/**
 * @Description: 应用service
 * @Author: yoga
 * @Date: 2024/8/30 16:23
 */
@Slf4j
@Service
public class ApplicationService {

    private final ApplicationMapper applicationMapper;

    public ApplicationService(ApplicationMapper applicationMapper) {
        this.applicationMapper = applicationMapper;
    }

    public void register(ApplicationDTO dto) {
        ApplicationDO application = JsonUtils.deepCopy(dto, ApplicationDO.class);
        application.setGmtCreated(LocalDateTime.now());
        applicationMapper.insert(application);
        log.info("应用[{}]注册成功！", dto.getName());
    }
}
