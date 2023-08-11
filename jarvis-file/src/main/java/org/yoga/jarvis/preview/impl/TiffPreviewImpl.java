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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @Description: tiff preview
 * @Author: yoga
 * @Date: 2023/8/10 16:28
 */
@Slf4j
public class TiffPreviewImpl extends AbstractPreview {

    @Override
    @NonNull
    protected File previewActual(@NonNull File srcFile, @NonNull File destDir) {
        File previewTmpFile = new File(destDir.getPath() + File.separator + UUID.randomUUID()
                + DelimiterType.point.getValue() + "pdf");

        Document document = new Document();
        RandomAccessFileOrArray rafa = null;
        try (OutputStream os = Files.newOutputStream(previewTmpFile.toPath());
             RandomAccessFile raf = new RandomAccessFile(srcFile, "r")) {
            PdfWriter.getInstance(document, os);
            document.open();
            rafa = new RandomAccessFileOrArray(new FileChannelRandomAccessSource(raf.getChannel()));
            int pages = TiffImage.getNumberOfPages(rafa);
            for (int i = 1; i <= pages; i++) {
                Image image = TiffImage.getTiffImage(rafa, i);
                image.scaleToFit(500, 900);
                document.add(image);
            }
        } catch (IOException | DocumentException e) {
            throw new JarvisException(e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (rafa != null) {
                try {
                    rafa.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return previewTmpFile;
    }
}