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

import com.aspose.cad.CodePages;
import com.aspose.cad.Color;
import com.aspose.cad.Image;
import com.aspose.cad.LoadOptions;
import com.aspose.cad.fileformats.cad.CadDrawTypeMode;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.DelimiterType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @Description: cad preview
 * @Author: yoga
 * @Date: 2023/8/14 16:32
 */
@Slf4j
public class CadPreviewImpl extends AbstractPreview {

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID()
                + DelimiterType.point.getValue() + "pdf");

        // srcFile options
        LoadOptions loadOptions = new LoadOptions();
        loadOptions.setSpecifiedEncoding(CodePages.SimpChinese);
        // pdf options
        CadRasterizationOptions cadRasterizationOptions = new CadRasterizationOptions();
        cadRasterizationOptions.setPageWidth(1600);
        cadRasterizationOptions.setPageHeight(1600);
        // auto Scaling
        cadRasterizationOptions.setAutomaticLayoutsScaling(true);
        cadRasterizationOptions.setNoScaling(false);
        cadRasterizationOptions.setDrawType(CadDrawTypeMode.UseDrawColor);
        cadRasterizationOptions.setBackgroundColor(Color.getWhite());
        PdfOptions pdfOptions = new PdfOptions();
        pdfOptions.setVectorRasterizationOptions(cadRasterizationOptions);
        try (Image cadImage = Image.load(srcFile.getPath(), loadOptions);
             OutputStream os = Files.newOutputStream(previewTmpFile.toPath())) {
            cadImage.save(os, pdfOptions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return previewTmpFile;
    }
}
