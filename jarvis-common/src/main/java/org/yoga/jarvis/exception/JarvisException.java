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

package org.yoga.jarvis.exception;

/**
 * @Description: jarvis exception
 * @Author: yoga
 * @Date: 2022/5/13 13:45
 */
public class JarvisException extends RuntimeException {

    private static final long serialVersionUID = -7109627487987559872L;

    /**
     * exception code
     */
    private int code;

    public JarvisException() {
        super();
    }

    public JarvisException(String message) {
        super(message);
    }

    public JarvisException(String message, Throwable cause) {
        super(message, cause);
    }

    public JarvisException(Throwable cause) {
        super(cause);
    }

    public JarvisException(int code) {
        super();
        this.code = code;
    }

    public JarvisException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JarvisException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public JarvisException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public JarvisException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
