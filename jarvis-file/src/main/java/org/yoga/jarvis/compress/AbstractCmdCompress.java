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

package org.yoga.jarvis.compress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.CmdHandler;
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
 * @Description: Abstract Cmd Compress
 * @Author: yoga
 * @Date: 2023/8/24 16:32
 */
@Slf4j
public abstract class AbstractCmdCompress extends AbstractCompress implements CmdHandler {

    @Override
    protected void compressActual(@NonNull File srcFile, @NonNull File destDir) {
        // check if the os supports the compress command
        checkCompressCmd();
        String shell = acquireShell(srcFile.getPath(), destDir.getPath());
        Assert.notBlank(shell, "compress shell is blank!");
        Process process = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec(shell);
            Assert.notNull(process, "compress cmd exec failed!");
            is = process.getInputStream();
            isReader = new InputStreamReader(is, Charset.forName("GBK"));
            br = new BufferedReader(isReader);
            String compressInfo = br.lines().collect(Collectors.joining("\n"));
            log.info("compress cmd:\n{}\nexec result:\n{}", shell, compressInfo);
            if (!isCmdCompressSuccess(compressInfo)) {
                throw new JarvisException(compressInfo);
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
     * check if the os supports the compress command
     */
    protected abstract void checkCompressCmd();

    /**
     * whether compress success
     * default: if the compress info is empty (no error message), it means success
     *
     * @param compressInfo compress info
     * @return whether compress success
     */
    protected boolean isCmdCompressSuccess(String compressInfo) {
        return StringUtils.isBlank(compressInfo);
    }

}
