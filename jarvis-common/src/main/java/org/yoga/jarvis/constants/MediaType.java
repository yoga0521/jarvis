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

package org.yoga.jarvis.constants;

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
    HTML("html", "text/html");

    /**
     * media name suffix
     */
    private final String suffix;

    /**
     * media type value
     */
    private final String value;

    MediaType(String suffix, String value) {
        this.suffix = suffix;
        this.value = value;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getValue() {
        return value;
    }

}
