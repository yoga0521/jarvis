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

package org.yoga.universe.exception;

/**
 * @Description: universe exception
 * @Author: yoga
 * @Date: 2022/5/13 13:45
 */
public class UniverseException extends RuntimeException {

    private static final long serialVersionUID = 8619644983741249030L;

    /**
     * exception code
     */
    private int code;

    public UniverseException() {
        super();
    }

    public UniverseException(String message) {
        super(message);
    }

    public UniverseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniverseException(Throwable cause) {
        super(cause);
    }

    public UniverseException(int code) {
        super();
        this.code = code;
    }

    public UniverseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public UniverseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public UniverseException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public UniverseException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
