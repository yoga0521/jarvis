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

package org.yoga.jarvis.spring.boot.autoconfigure.identifier;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

/**
 * @Description: the properties object of identifier
 * @Author: yoga
 * @Date: 2022/7/18 17:42
 */
@ConfigurationProperties(prefix = "jarvis.id")
public class IdentifierProperties {

    /**
     * work machine ID (0~31)
     */
    private long workerId;

    /**
     * data identification ID (0~31)
     */
    private long dataCenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IdentifierProperties.class.getSimpleName() + "[", "]")
                .add("workerId=" + workerId)
                .add("dataCenterId=" + dataCenterId)
                .toString();
    }
}
