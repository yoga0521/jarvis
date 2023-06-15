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

package org.yoga.jarvis.util;

import net.lingala.zip4j.ZipFile;
import org.apache.tika.Tika;
import org.yoga.jarvis.constant.MediaType;
import org.yoga.jarvis.exception.JarvisException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @Description: FileUtils
 * @Author: yoga
 * @Date: 2023/4/10 10:06
 */
public class FileUtils {

    /**
     * UNZIP PATH
     */
    private static final String UNZIP_PATH = System.getProperty("user.dir") + "/file_unzip_tmp";

    /**
     * NUMBER PATTERN
     */
    private static final String NUMBER_PATTERN = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";

    /**
     * unzip shell
     * -n:do not overwrite existing files
     * -q:quiet mode, do not display the execution process of the command
     * -d:specify unzip path, will be created automatically if the path does not exist
     */
    private static final String UNZIP_SHELL = "unzip -n -q %s -d %s";

    /**
     * unrar shell
     * the decompress path must end with slash
     * x:unrar to full path
     * -r:recursive unrar
     * -ad:append file name to target path
     * -y:set all answers to yes
     */
    private static final String UNRAR_SHELL = "unrar x -ad -r -y %s %s";

    /**
     * unzip shell
     * x:unzip to full path
     * -r:recursive unzip 7z
     * -y:set all answers to yes
     * -o:specify unzip 7z path, -o directly to the path (no spaces)
     */
    private static final String UN7ZIP_SHELL = "7z x -y %s -o%s";

    /**
     * key info for unrar decompress successful
     */
    private static final String UNRAR_SUCCESS_KEY_MSG = "All OK";

    /**
     * key info for 7z decompress successful
     */
    private static final String UN7ZIP_SUCCESS_KEY_MSG = "Everything is Ok";

    /**
     * Get the file name suffix
     *
     * @param fileName file name
     * @return suffix
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".") || fileName.endsWith(".")) {
            return null;
        }
        String[] arr = fileName.split("\\.");
        if (ArrayUtils.isEmpty(arr)) {
            return null;
        }
        return arr[arr.length - 1];
    }

    /**
     * Get the file name without suffix
     *
     * @param fileName file name
     * @return file name without suffix
     */
    public static String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName) || fileName.startsWith(".")) {
            return null;
        }
        return !fileName.contains(".") ? fileName : fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * make files sorted according to WindowsOS sorting rules
     * tips: null last
     *
     * @param fileList file list
     */
    public static void sortByWindowsRule(List<File> fileList) {
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }
        fileList.sort((f1, f2) -> {
            if (f1 == null && f2 == null) {
                return 0;
            } else if (f2 == null) {
                return -1;
            } else if (f1 == null) {
                return 1;
            }
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            }
            if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            }
            if (StringUtils.isNotBlank(f1.getName()) && StringUtils.isBlank(f2.getName())) {
                return -1;
            }
            if (StringUtils.isBlank(f1.getName()) && StringUtils.isNotBlank(f2.getName())) {
                return 1;
            }
            String[] arr1 = f1.getName().split(NUMBER_PATTERN);
            String[] arr2 = f2.getName().split(NUMBER_PATTERN);
            int i = 0;
            while (i < arr1.length && i < arr2.length) {
                if (arr1[i].equals(arr2[i])) {
                    i++;
                } else if (NumberUtils.isBigDecimal(arr1[i]) && NumberUtils.isBigDecimal(arr2[i])) {
                    return new BigDecimal(arr1[i]).compareTo(new BigDecimal(arr2[i]));
                } else {
                    return Collator.getInstance(Locale.CHINA).compare(arr1[i], arr2[i]);
                }
            }
            return arr1.length - arr2.length;
        });
    }

    /**
     * unzip file
     *
     * @param file file
     */
    public static void unzip(File file) {
        unzip(file, null, null);
    }

    /**
     * unzip file
     *
     * @param file     file
     * @param password unzip password
     */
    public static void unzip(File file, String password) {
        unzip(file, password, null);
    }

    /**
     * unzip file
     *
     * @param file      file
     * @param password  unzip password
     * @param unzipPath unzip path
     */
    public static void unzip(File file, String password, String unzipPath) {
        if (file == null) {
            throw new JarvisException("file is null!");
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            if (zipFile.isEncrypted()) {
                if (StringUtils.isBlank(password)) {
                    throw new JarvisException("password is blank!");
                }
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.extractAll(StringUtils.isBlank(unzipPath) ? UNZIP_PATH : unzipPath);
        } catch (IOException e) {
            throw new JarvisException("unzip fail!", e);
        }
    }

    /**
     * decompress
     *
     * @param srcFile     source file
     * @param destTmpPath temp directory for decompressed files
     */
    public static void decompress(File srcFile, String destTmpPath) {
        Assert.notNull(srcFile, "srcFile is null");
        Assert.notBlank(destTmpPath, "decompress temp dir path is empty");
        Process process = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader br = null;
        long startTime = System.currentTimeMillis();
        try {
            String val = new Tika().detect(srcFile);
            MediaType mediaType = MediaType.getByValue(val);
            Assert.notNull(mediaType, "this type (" + val + ") of file is not support decompress");

            String command;
            String decompressCommand;
            String decompressSuccessKeyMsg;
            // rar
            if (MediaType.APPLICATION_RAR.equals(mediaType)) {
                command = "unrar";
                decompressCommand = String.format(UNRAR_SHELL, srcFile.getPath(),
                        destTmpPath.endsWith(File.separator) ? destTmpPath : (destTmpPath + File.separator));
                decompressSuccessKeyMsg = UNRAR_SUCCESS_KEY_MSG;
            }
            // 7z
            else if (MediaType.APPLICATION_7Z.equals(mediaType)) {
                command = "7z";
                decompressCommand = String.format(UN7ZIP_SHELL, srcFile.getPath(), destTmpPath);
                decompressSuccessKeyMsg = UN7ZIP_SUCCESS_KEY_MSG;
            }
            // zip
            else {
                command = "unzip";
                decompressCommand = String.format(UNZIP_SHELL, srcFile.getPath(), destTmpPath);
                decompressSuccessKeyMsg = "";
            }
            Assert.isTrue(OSUtils.checkCommand(command), "the system is not support " + command + " command");
            // 解压
            process = Runtime.getRuntime().exec(decompressCommand);
            Assert.notNull(process, "decompress command execution failed");
            is = process.getInputStream();
            isReader = new InputStreamReader(is, Charset.forName("GBK"));
            br = new BufferedReader(isReader);
            String commandResult = br.lines().collect(Collectors.joining("\n"));
            boolean noErrorMsg = StringUtils.isBlank(commandResult) && StringUtils.isBlank(decompressSuccessKeyMsg);
            boolean hasSuccessMsg = StringUtils.isNotBlank(commandResult) && commandResult.replace("\n", "").contains(decompressSuccessKeyMsg);
            if (!noErrorMsg && !hasSuccessMsg) {
                throw new JarvisException(commandResult);
            }
        } catch (IOException e) {
            throw new JarvisException(e.getMessage(), e);
        } finally {
            IOUtils.close(br);
            IOUtils.close(isReader);
            IOUtils.close(is);
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static void main(String[] args) {
//        File file = new File("/Users/yoga/Downloads");
//        if (file.exists() && ArrayUtils.isNotEmpty(file.listFiles())) {
//            System.out.println(file.listFiles().length);
//            List<File> files = new LinkedList<>();
//            for (File f : file.listFiles()) {
//                files.add(f);
//            }
//            sortByWindowsRule(files);
//            files.forEach(f -> System.out.println(f.getName()));
//        }
        File file = new File("/Users/yoga/Downloads/mysql-connector-java-5.1.46.zip");
        unzip(file, null, "/Users/yoga/Downloads/tmp");
    }
}
