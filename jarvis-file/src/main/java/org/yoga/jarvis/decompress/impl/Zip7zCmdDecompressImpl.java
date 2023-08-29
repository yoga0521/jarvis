package org.yoga.jarvis.decompress.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.decompress.AbstractCmdDecompress;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.OSUtils;
import org.yoga.jarvis.util.StringUtils;

/**
 * @Description: 7zip cmd decompress
 * @Author: yoga
 * @Date: 2023/6/27 13:37
 */
@Slf4j
public class Zip7zCmdDecompressImpl extends AbstractCmdDecompress {

    /**
     * 7z shell
     * x:7z unzip to full path
     * -r:recursive 7z unzip
     * -y:set all answers to yes
     * -o:specify 7z unzip path, -o directly to the path (no spaces)
     */
    private static final String UN7ZIP_SHELL = "7z x -y %s -o%s";

    @NonNull
    @Override
    public String acquireShell(@NonNull String srcFilePath, @NonNull String destDirPath) {
        // macOS安装了7z命令
        return String.format(UN7ZIP_SHELL, srcFilePath, destDirPath);
    }

    @Override
    protected void checkDecompressCmd() {
        Assert.isTrue(OSUtils.checkCommand("7z"), "The OS don't support 7z decompress command!");
    }

    /**
     * 7z is success
     *
     * @param decompressInfo decompress info
     * @return is success
     */
    @Override
    protected boolean isCmdDecompressSuccess(String decompressInfo) {
        return StringUtils.isNotBlank(decompressInfo)
                && decompressInfo.replace("\n", "").contains("Everything is Ok");
    }

}
