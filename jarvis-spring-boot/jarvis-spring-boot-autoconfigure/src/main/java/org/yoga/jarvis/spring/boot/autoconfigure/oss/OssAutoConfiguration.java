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

package org.yoga.jarvis.spring.boot.autoconfigure.oss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yoga.jarvis.AbstractOssHandler;
import org.yoga.jarvis.DefaultOssHandler;
import org.yoga.jarvis.MultipartOssHandler;
import org.yoga.jarvis.OssHandler;
import org.yoga.jarvis.bean.OssConfigs;

/**
 * @Description: oss auto configure
 * @Author: yoga
 * @Date: 2022/5/25 10:26
 */
@Configuration
@ConditionalOnProperty(prefix = "jarvis.oss", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({OssHandler.class, AbstractOssHandler.class, DefaultOssHandler.class, MultipartOssHandler.class})
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    private final OssProperties ossProperties;

    public OssAutoConfiguration(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * create DefaultOssHandler Bean
     *
     * @return DefaultOssHandler {@link org.yoga.jarvis.DefaultOssHandler}
     */
    @Bean("defaultOssHandler")
    @ConditionalOnMissingBean(DefaultOssHandler.class)
    OssHandler defaultOssHandler() {
        return new DefaultOssHandler(trans2OssConfigs(ossProperties));
    }

    /**
     * create MultipartOssHandler Bean
     *
     * @return MultipartOssHandler {@link org.yoga.jarvis.MultipartOssHandler}
     */
    @Bean("multipartOssHandler")
    @ConditionalOnMissingBean(MultipartOssHandler.class)
    OssHandler multipartOssHandler() {
        return new MultipartOssHandler(trans2OssConfigs(ossProperties));
    }

    /**
     * OssProperties to Oss Configs
     *
     * @param ossProperties ossProperties {@link OssProperties}
     * @return ossConfigs {@link OssConfigs}
     */
    OssConfigs trans2OssConfigs(OssProperties ossProperties) {
        OssConfigs ossConfigs = new OssConfigs();
        ossConfigs.setEndpoint(ossProperties.getEndpoint());
        ossConfigs.setBucketName(ossProperties.getBucketName());
        ossConfigs.setAccessKeyId(ossProperties.getAccessKeyId());
        ossConfigs.setAccessKeySecret(ossProperties.getAccessKeySecret());
        ossConfigs.setPartSize(ossProperties.getPartSize());
        return ossConfigs;
    }
}
