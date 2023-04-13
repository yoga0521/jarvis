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

import java.io.File;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

/**
 * @Description: FileUtils
 * @Author: yoga
 * @Date: 2023/4/10 10:06
 */
public class FileUtils {

    /**
     * NUMBER PATTERN
     */
    private static final String NUMBER_PATTERN = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";

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

    public static void main(String[] args) {

    }
}
