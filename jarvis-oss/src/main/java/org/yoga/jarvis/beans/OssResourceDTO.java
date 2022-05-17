/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis.beans;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Description: oss resource eq:pdf,docx,xlsx,png...
 * @Author: yoga
 * @Date: 2022/5/13 14:18
 */
public class OssResourceDTO implements Serializable {

    private static final long serialVersionUID = -4123504256533313281L;

    /**
     * name of resource
     */
    private String resourceName;

    /**
     * oss objectName
     */
    private String objectName;

    /**
     * size of resource(kb)
     */
    private Long resourceSize;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }


    public Long getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(Long resourceSize) {
        this.resourceSize = resourceSize;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OssResourceDTO.class.getSimpleName() + "[", "]")
                .add("resourceName='" + resourceName + "'")
                .add("objectName='" + objectName + "'")
                .add("resourceSize=" + resourceSize)
                .toString();
    }
}
