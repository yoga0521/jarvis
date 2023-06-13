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

package org.yoga.jarvis.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: media type
 * @Author: yoga
 * @Date: 2022/5/12 11:35
 */
public enum MediaType {

    /**
     * word/doc
     */
    DOC("doc", "application/msword"),

    /**
     * word/docx
     */
    DOCX("docx", "application/msword"),

    /**
     * excel/xls
     */
    XLS("xls", "text/xml"),

    /**
     * excel/xlsx
     */
    XLSX("xlsx", "text/xml"),

    /**
     * ppt
     */
    PPT("ppt", "application/x-ppt"),

    /**
     * pptx
     */
    PPTX("pptx", "application/x-ppt"),

    /**
     * pdf
     */
    PDF("pdf", "application/pdf"),

    /**
     * html
     */
    HTML("html", "text/html"),

    /**
     * zip
     */
    APPLICATION_ZIP("zip", "application/zip"),

    /**
     * rar
     */
    APPLICATION_RAR("rar", "application/x-rar-compressed"),

    /**
     * 7z
     */
    APPLICATION_7Z("7z", "application/x-7z-compressed");

    /**
     * media name suffix
     */
    private final String suffix;

    /**
     * media type value
     */
    private final String value;

    private final static Map<String, MediaType> VALUE_MAP = new HashMap<>(MediaType.values().length);

    MediaType(String suffix, String value) {
        this.suffix = suffix;
        this.value = value;
    }

    static {
        for (MediaType type : MediaType.values()) {
            VALUE_MAP.put(type.getValue(), type);
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public String getValue() {
        return value;
    }

    /**
     * get mediaType by value
     *
     * @param value media type value
     * @return mediaType
     */
    public static MediaType getByValue(String value) {
        return VALUE_MAP.get(value);
    }
}
