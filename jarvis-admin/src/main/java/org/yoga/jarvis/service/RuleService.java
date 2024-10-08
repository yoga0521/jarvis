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
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.yoga.jarvis.bean.constant.CommonConstant;
import org.yoga.jarvis.bean.dto.RuleDTO;
import org.yoga.jarvis.repository.entity.RouteRuleDO;
import org.yoga.jarvis.repository.mapper.RouteRuleMapper;
import org.yoga.jarvis.util.JsonUtils;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<RuleDTO> getEnabledRules() {
        QueryWrapper<RouteRuleDO> query = new QueryWrapper<>();
        query.eq(RouteRuleDO.ENABLED, CommonConstant.YES);
        query.eq(RouteRuleDO.IS_DELETE, CommonConstant.IS_DEL_NOT);
        return JsonUtils.deepCopy(routeRuleMapper.selectList(query), new TypeReference<List<RuleDTO>>() {
        });
    }

    public void addRule(RuleDTO ruleDTO) {
        RouteRuleDO routeRule = JsonUtils.deepCopy(ruleDTO, RouteRuleDO.class);
        routeRuleMapper.insert(routeRule);
        // 监听 todo
    }
}
