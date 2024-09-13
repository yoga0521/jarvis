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
import org.yoga.jarvis.repository.mapper.RouteRuleMapper;

/**
 * @Description: 规则service
 * @Author: yoga
 * @Date: 2024/9/10 13:32
 */
@Slf4j
@Service
public class RuleService {

    private final RouteRuleMapper routeRuleMapper;

    public RuleService(RouteRuleMapper routeRuleMapper) {
        this.routeRuleMapper = routeRuleMapper;
    }
}
