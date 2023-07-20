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

package org.yoga.jarvis.constant;

import java.nio.charset.Charset;

/**
 * @Description: charset
 * @Author: yoga
 * @Date: 2023/7/19 15:35
 */
public enum Charsets {

    /**
     * GBK
     */
    GBK("GBK", Charset.forName("GBK")),

    /**
     * UTF-8
     */
    UTF_8("UTF-8", Charset.forName("UTF-8")),

    /**
     * ISO-8859-1
     */
    ISO_8859_1("ISO-8859-1", Charset.forName("ISO-8859-1"));

    /**
     * charset name
     */
    private final String charsetName;

    /**
     * charset
     */
    private final Charset charset;

    Charsets(String charsetName, Charset charset) {
        this.charsetName = charsetName;
        this.charset = charset;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public Charset getCharset() {
        return charset;
    }
}
