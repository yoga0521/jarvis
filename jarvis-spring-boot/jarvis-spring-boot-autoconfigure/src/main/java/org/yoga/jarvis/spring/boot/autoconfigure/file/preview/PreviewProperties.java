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

package org.yoga.jarvis.spring.boot.autoconfigure.file.preview;

/**
 * @Description: the properties object of preview
 * @Author: yoga
 * @Date: 2023/8/18 11:11
 */
public class PreviewProperties {

    /**
     * whether to enable oss autoconfiguration
     */
    private boolean enabled = true;

    /**
     * office home
     */
    private String officeHome;

    /**
     * host name
     */
    private String hostName;

    /**
     * list of ports
     */
    private int[] portNumbers = {2002};

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getOfficeHome() {
        return officeHome;
    }

    public void setOfficeHome(String officeHome) {
        this.officeHome = officeHome;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int[] getPortNumbers() {
        return portNumbers;
    }

    public void setPortNumbers(int[] portNumbers) {
        this.portNumbers = portNumbers;
    }
}
