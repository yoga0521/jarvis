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

package org.yoga.jarvis.decompress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * @Description: cmd abstract decompress
 * @Author: yoga
 * @Date: 2023/6/21 16:17
 */
@Slf4j
public abstract class AbstractCmdDecompress extends AbstractDecompress {

    @Override
    protected void decompressActual(@NonNull File srcFile, @NonNull File destDir) {
        // check if the os supports the decompress command
        checkDecompressCmd();
        String shell = acquireShell(srcFile.getPath(), destDir.getPath());
        Assert.notBlank(shell, "decompress shell is blank!");
        Process process = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec(shell);
            Assert.notNull(process, "decompress cmd exec failed!");
            is = process.getInputStream();
            isReader = new InputStreamReader(is, Charset.forName("GBK"));
            br = new BufferedReader(isReader);
            String decompressInfo = br.lines().collect(Collectors.joining("\n"));
            log.info("decompress cmd:\n{}\nexec result:\n{}", shell, decompressInfo);
            if (!isCmdDecompressSuccess(decompressInfo)) {
                throw new JarvisException(decompressInfo);
            }
        } catch (IOException e) {
            throw new JarvisException(e);
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(isReader);
            IOUtils.closeQuietly(is);
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * acquire the decompress shell script
     */
    @NonNull
    protected abstract String acquireShell(@NonNull String srcFilePath, @NonNull String destDirPath);

    /**
     * check if the os supports the decompress command
     */
    protected abstract void checkDecompressCmd();

    /**
     * whether decompress success
     * default: if the decompress info is empty (no error message), it means success
     *
     * @param decompressInfo decompress info
     * @return whether decompress success
     */
    protected boolean isCmdDecompressSuccess(String decompressInfo) {
        return StringUtils.isBlank(decompressInfo);
    }
}
