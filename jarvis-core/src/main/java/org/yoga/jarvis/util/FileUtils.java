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
import org.yoga.jarvis.exception.JarvisException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
     * @param file     file
     * @param password unzip password
     */
    private static void unzip(File file, char[] password) {
        try (ZipFile zipFile = new ZipFile(file)) {
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(UNZIP_PATH);
        } catch (IOException e) {
            throw new JarvisException("unzip fail", e);
        }
    }

    public static void main(String[] args) {
        File file = new File("/Users/yoga/Downloads");
        if (file.exists() && ArrayUtils.isNotEmpty(file.listFiles())) {
            System.out.println(file.listFiles().length);
            List<File> files = new LinkedList<>();
            for (File f : file.listFiles()) {
                files.add(f);
            }
            sortByWindowsRule(files);
            files.forEach(f -> System.out.println(f.getName()));
        }
    }
}
