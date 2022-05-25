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

package org.yoga.jarvis.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: I/O utils
 * @Author: yoga
 * @Date: 2022/5/13 16:13
 */
public class IOUtils {

    /**
     * The default buffer size ({@value}) ,
     * be consistent with {@link java.io.BufferedInputStream} {@code DEFAULT_BUFFER_SIZE}
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * {@code InputStream} convert to {@code byte[]}
     *
     * @param input the {@code InputStream} to read
     * @return byte array
     * @throws IllegalArgumentException if {@code InputStream} is null
     * @throws IOException              if an I/O error occurs
     */
    public static byte[] toByteArray(final InputStream input) throws IOException {
        Assert.notNull(input, "input must not be null!");

        // close ByteArrayOutputStream
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
    }
}
