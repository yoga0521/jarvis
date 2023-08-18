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

package org.yoga.jarvis.spring.boot.autoconfigure.file.preview;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: preview auto configure
 * @Author: yoga
 * @Date: 2023/8/18 12:02
 */
@Configuration
@ConditionalOnProperty(prefix = "jarvis.file.preview", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(PreviewProperties.class)
public class PreviewAutoConfiguration {

    private final PreviewProperties previewProperties;

    public PreviewAutoConfiguration(PreviewProperties previewProperties) {
        this.previewProperties = previewProperties;
    }
}
