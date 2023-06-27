package org.yoga.jarvis.decompress.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.decompress.AbstractDecompress;
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;

/**
 * @Description: other decompress
 * @Author: yoga
 * @Date: 2023/6/27 12:05
 */
@Slf4j
public class OtherDecompressImpl extends AbstractDecompress {

    @Override
    protected void decompressActual(@NonNull File srcFile, @NonNull File destDir) {
        try {

            String fileType = new Tika().detect(srcFile);
            log.error("It don't support the decompression of the {} file type!", fileType);
            throw new JarvisException("It don't support the decompression of the " + fileType + " file type");
        } catch (IOException e) {
            log.error("Other decompress impl class fail to get the file type", e);
            throw new JarvisException("It don't support the decompression of the " + FilenameUtils.getExtension(srcFile.getName()) + " file type");
        }
    }
}
