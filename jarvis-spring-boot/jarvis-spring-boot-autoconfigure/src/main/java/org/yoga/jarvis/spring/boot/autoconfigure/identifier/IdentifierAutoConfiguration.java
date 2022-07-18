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

package org.yoga.jarvis.spring.boot.autoconfigure.identifier;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.yoga.jarvis.SnowFlakeIdGenerator;

/**
 * @Description: TODO
 * @Author: yoga
 * @Date: 2022/7/18 17:46
 */
@Configuration
@ConditionalOnClass(SnowFlakeIdGenerator.class)
@EnableConfigurationProperties(IdentifierProperties.class)
public class IdentifierAutoConfiguration {

    private final IdentifierProperties identifierProperties;


    public IdentifierAutoConfiguration(IdentifierProperties identifierProperties) {
        this.identifierProperties = identifierProperties;
    }
}
