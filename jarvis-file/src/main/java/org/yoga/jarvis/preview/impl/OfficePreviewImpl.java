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

package org.yoga.jarvis.preview.impl;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistryInstanceHolder;
import org.jodconverter.core.document.DocumentFormatRegistry;
import org.jodconverter.core.document.JsonDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.local.office.LocalOfficeUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.MediaType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.preview.bean.OfficeLocalConverterConfigs;
import org.yoga.jarvis.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Description: office file preview
 * @Author: yoga
 * @Date: 2023/7/14 17:25
 */
@Slf4j
public class OfficePreviewImpl extends AbstractPreview {

    private static final String DEFAULT_FORMATS_PATH = "classpath:document-formats.json";

    private static final String CUSTOM_FORMATS_PATH = "classpath:custom-document-formats.json";

    protected final OfficeLocalConverterConfigs configs;

    protected final DocumentConverter documentConverter;

    protected OfficePreviewImpl(ResourceLoader resourceLoader, OfficeLocalConverterConfigs configs) {
        Assert.notNull(resourceLoader, "resourceLoader isn't exist!");
        this.configs = configs;

        final OfficeManager localOfficeManager =
                LocalOfficeManager.builder()
                        .officeHome(configs.getOfficeHome())
                        .hostName(configs.getHostName())
                        .portNumbers(configs.getPortNumbers())
//                        .workingDir(configs.getWorkingDir())
//                        .templateProfileDir(configs.getTemplateProfileDir())
//                        .existingProcessAction(configs.getExistingProcessAction())
//                        .processTimeout(configs.getProcessTimeout())
//                        .processRetryInterval(configs.getProcessRetryInterval())
//                        .afterStartProcessDelay(configs.getAfterStartProcessDelay())
//                        .disableOpengl(configs.isDisableOpengl())
//                        .startFailFast(configs.isStartFailFast())
//                        .keepAliveOnShutdown(configs.isKeepAliveOnShutdown())
//                        .taskQueueTimeout(configs.getTaskQueueTimeout())
//                        .taskExecutionTimeout(configs.getTaskExecutionTimeout())
//                        .maxTasksPerProcess(configs.getMaxTasksPerProcess())
                        .processManager(LocalOfficeUtils.findBestProcessManager())
                        .build();


        log.debug("loading document formats registry from resource [{}]", DEFAULT_FORMATS_PATH);

        final DocumentFormatRegistry documentFormatRegistry;
        try (InputStream in = resourceLoader.getResource(DEFAULT_FORMATS_PATH).getInputStream()) {

            // Create the registry.
            final JsonDocumentFormatRegistry registry = JsonDocumentFormatRegistry.create(in);

            // Load the custom formats, if any.
            final Resource resource = resourceLoader.getResource(CUSTOM_FORMATS_PATH);
            if (resource.exists()) {
                log.debug("loading custom document formats registry from resource [{}]", CUSTOM_FORMATS_PATH);
                registry.addRegistry(JsonDocumentFormatRegistry.create(resource.getInputStream()));
            }

            // Set as default.
            DefaultDocumentFormatRegistryInstanceHolder.setInstance(registry);
            documentFormatRegistry = registry;
        } catch (IOException e) {
            throw new JarvisException("create DocumentFormatRegistry fail!", e);
        }

        this.documentConverter = LocalConverter.builder()
                .officeManager(localOfficeManager)

                .formatRegistry(documentFormatRegistry)
                // use default load document mode
                .loadDocumentMode(LocalConverter.DEFAULT_LOAD_DOCUMENT_MODE)
                // use default load properties
                .loadProperties(LocalConverter.DEFAULT_LOAD_PROPERTIES)
                .build();
    }

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID() +
                (MediaType.isOfficePreviewByHtml(srcFile.getName()) ? ".html" : ".pdf"));
        try {
            documentConverter.convert(srcFile).to(previewTmpFile).execute();
        } catch (OfficeException e) {
            throw new JarvisException(e);
        }
        return previewTmpFile;
    }
}
