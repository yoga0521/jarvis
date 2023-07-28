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

import org.yoga.jarvis.util.FileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
     * The suffix type that needs to be converted to html preview
     */
    private static final String[] PREVIEW_BY_HTML_SUFFIX_TYPE = {"xls", "xlsx", "csv", "xlsm", "et", "xlt", "xltm", "ett", "xlam"};

    /**
     * The suffix type of office file
     */
    private static final String[] OFFICE_SUFFIX_TYPES = {"docx", "wps", "doc", "docm", "xls", "xlsx", "csv", "xlsm", "ppt", "pptx", "vsd", "rtf", "odt", "wmf", "emf", "dps", "et", "ods", "ots", "tsv", "odp", "otp", "sxi", "ott", "vsdx", "fodt", "fods", "xltx", "tga", "psd", "dotm", "ett", "xlt", "xltm", "wpt", "dot", "xlam", "dotx", "xla", "pages"};

    /**
     * The suffix type for simple files
     */
    private static final String[] SIM_TEXT_SUFFIX_TYPES = {"txt", "html", "htm", "asp", "jsp", "xml", "json", "properties", "md", "gitignore", "log", "java", "py", "c", "cpp", "sql", "sh", "bat", "m", "bas", "prg", "cmd"};

    /**
     * multimedia type
     */
    private static final String[] MEDIA_SUFFIX_TYPES = {"mp3", "wav", "mp4", "flv", "rmvb"};

    /**
     * The suffix type of the file that can be directly previewed
     */
    private static final String[] CAN_PREVIEW_DIRECTLY_SUFFIX_TYPES = {"jpg", "jpeg", "png", "gif", "bmp", "ico", "jfif", "webp", "pdf"};


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

    /**
     * Does the office document need to be converted to html preview
     *
     * @param fileName file name
     * @return dose need to convert to html preview
     */
    public static boolean isOfficePreviewByHtml(String fileName) {
        return Stream.of(PREVIEW_BY_HTML_SUFFIX_TYPE)
                .anyMatch(suffix -> suffix.equalsIgnoreCase(FileUtils.getFileSuffix(fileName)));
    }
}
