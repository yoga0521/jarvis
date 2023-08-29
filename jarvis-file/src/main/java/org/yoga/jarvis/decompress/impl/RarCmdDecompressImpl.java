package org.yoga.jarvis.decompress.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.yoga.jarvis.decompress.AbstractCmdDecompress;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.OSUtils;
import org.yoga.jarvis.util.StringUtils;

import java.io.File;

/**
 * @Description: rar cmd decompress
 * @Author: yoga
 * @Date: 2023/6/26 10:03
 */
@Slf4j
public class RarCmdDecompressImpl extends AbstractCmdDecompress {

    /**
     * unrar shell
     * the decompress path must end with slash
     * x:unrar to full path
     * -r:recursive unrar
     * -ad:append file name to target path
     * -y:set all answers to yes
     */
    private static final String UNRAR_SHELL = "unrar x -ad -y %s %s";

    @NonNull
    @Override
    public String acquireShell(@NonNull String srcFilePath, @NonNull String destDirPath) {
        return String.format(UNRAR_SHELL, srcFilePath,
                destDirPath.endsWith(File.separator) ? destDirPath : (destDirPath + File.separator));
    }

    @Override
    protected void checkDecompressCmd() {
        Assert.isTrue(OSUtils.checkCommand("unrar"), "The OS don't support unrar decompress command!");
    }

    /**
     * unrar is success
     *
     * @param decompressInfo decompress info
     * @return is success
     */
    @Override
    protected boolean isCmdDecompressSuccess(String decompressInfo) {
        return StringUtils.isNotBlank(decompressInfo)
                && decompressInfo.replace("\n", "").contains("All OK");
    }
}
