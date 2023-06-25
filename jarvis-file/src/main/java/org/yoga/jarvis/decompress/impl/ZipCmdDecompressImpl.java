package org.yoga.jarvis.decompress.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.decompress.AbstractCmdDecompress;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.OSUtils;

/**
 * @Description: zip cmd decompress
 * @Author: yoga
 * @Date: 2023/6/20 16:02
 */
@Slf4j
public class ZipCmdDecompressImpl extends AbstractCmdDecompress {

    /**
     * unzip shell
     * -n:do not overwrite existing files
     * -q:quiet mode, do not display the execution process of the command
     * -d:specify unzip path, will be created automatically if the path does not exist
     */
    private static final String UNZIP_SHELL = "unzip -n -q %s -d %s";

    @NonNull
    @Override
    protected String acquireShell(@NonNull String srcFilePath, @NonNull String destDirPath) {
        return String.format(UNZIP_SHELL, srcFilePath, destDirPath);
    }

    @Override
    protected void checkDecompressCmd() {
        Assert.isTrue(OSUtils.checkCommand("unzip"), "The OS don‘t support unzip decompress command!");
    }
}
