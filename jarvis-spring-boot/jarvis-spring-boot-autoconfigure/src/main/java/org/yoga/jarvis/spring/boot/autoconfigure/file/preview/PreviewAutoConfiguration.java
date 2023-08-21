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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.yoga.jarvis.preview.Preview;
import org.yoga.jarvis.preview.bean.OfficeLocalConverterConfigs;
import org.yoga.jarvis.preview.impl.AbstractPreview;
import org.yoga.jarvis.preview.impl.OfficePreviewImpl;

/**
 * @Description: preview auto configure
 * @Author: yoga
 * @Date: 2023/8/18 12:02
 */
@Configuration
@ConditionalOnProperty(prefix = "jarvis.file.preview", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Preview.class, AbstractPreview.class, OfficePreviewImpl.class})
@EnableConfigurationProperties(PreviewProperties.class)
public class PreviewAutoConfiguration {

    private final PreviewProperties previewProperties;

    public PreviewAutoConfiguration(PreviewProperties previewProperties) {
        this.previewProperties = previewProperties;
    }

    @Bean("officePreview")
    @ConditionalOnMissingBean(OfficePreviewImpl.class)
    OfficePreviewImpl OfficePreview(final ResourceLoader resourceLoader) {
        return new OfficePreviewImpl(resourceLoader, trans2OfficeLocalconverterConfigs(previewProperties));
    }

    /**
     * PreviewProperties to Office Preview Configs
     *
     * @param previewProperties previewProperties {@link PreviewProperties}
     * @return officeLocalConverterConfigs {@link OfficeLocalConverterConfigs}
     */
    OfficeLocalConverterConfigs trans2OfficeLocalconverterConfigs(PreviewProperties previewProperties) {
        OfficeLocalConverterConfigs officeLocalConverterConfigs = new OfficeLocalConverterConfigs();
        officeLocalConverterConfigs.setEnabled(previewProperties.isEnabled());
        officeLocalConverterConfigs.setOfficeHome(previewProperties.getOfficeHome());
        officeLocalConverterConfigs.setHostName(previewProperties.getHostName());
        if (previewProperties.getPortNumbers() != null && previewProperties.getPortNumbers().length > 0) {
            officeLocalConverterConfigs.setPortNumbers(previewProperties.getPortNumbers());
        }
        return officeLocalConverterConfigs;
    }
}
