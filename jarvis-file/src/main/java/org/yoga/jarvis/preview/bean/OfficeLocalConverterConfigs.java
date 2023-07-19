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

package org.yoga.jarvis.preview.bean;

import org.jodconverter.local.office.LocalOfficeManager;

/**
 * @Description: Office Local Converter Configs
 * @Author: yoga
 * @Date: 2023/7/17 11:11
 */
public class OfficeLocalConverterConfigs {

    /**
     * Enable JODConverter, which means that office instances will be launched.
     */
    private boolean enabled;

    /**
     * Represents the office home directory. If not set, the office installation directory is
     * auto-detected, most recent version of LibreOffice first.
     */
    private String officeHome;

    /**
     * Host name that will be use in the --accept argument when starting an office process. Most of
     * the time, the default will work. But if it doesn't work (unable to connect to the started
     * process), using 'localhost' as host name instead may work.
     */
    private String hostName = LocalOfficeManager.DEFAULT_HOSTNAME;

    /**
     * List of ports, separated by commas, used by each JODConverter processing thread. The number of
     * office instances is equal to the number of ports, since 1 office process will be launched for
     * each port number.
     */
    private int[] portNumbers = {2002};
}
