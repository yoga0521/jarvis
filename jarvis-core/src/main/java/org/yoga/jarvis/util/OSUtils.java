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

import org.yoga.jarvis.exception.JarvisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * @Description: OS Utils
 * @Author: yoga
 * @Date: 2023/6/5 17:48
 */
public class OSUtils {

    /**
     * windows
     */
    public static final String WINDOWS = "Windows";

    /**
     * mac
     */
    public static final String MAC = "Mac OS";

    /**
     * is Windows OS
     *
     * @return is Windows OS
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").contains(WINDOWS);
    }

    /**
     * is Mac OS
     *
     * @return is Mac OS
     */
    public static boolean isMAC() {
        return System.getProperty("os.name").contains(MAC);
    }

    /**
     * check command is installed
     *
     * @param command command
     * @return command is installed
     */
    public static boolean checkCommand(String command) {
        if (StringUtils.isBlank(command)) {
            throw new JarvisException("command is blank!");
        }
        command = command.split(" ")[0];
        String noCommandKeyInfo = isMAC() ? "not found" : String.format("no %s in", command);
        Process process = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec("which " + command);
            if (process == null) {
                throw new JarvisException("check command fail!");
            }
            is = process.getInputStream();
            isReader = new InputStreamReader(is, Charset.forName("GBK"));
            br = new BufferedReader(isReader);
            String commandResult = br.lines().collect(Collectors.joining());
            return StringUtils.isNotBlank(commandResult) && !commandResult.contains(noCommandKeyInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JarvisException("check command fail!" + e.getMessage());
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
        System.out.println(checkCommand("java"));
        System.out.println(checkCommand("abc"));
    }
}
