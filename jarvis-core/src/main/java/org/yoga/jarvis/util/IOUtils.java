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

import org.yoga.jarvis.exception.JarvisException;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

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

    /**
     * read object from stream
     *
     * @param in  input stream
     * @param <T> generics
     * @return object
     * @throws JarvisException    throw JarvisException
     * @throws ClassCastException if the type convert fail, then throw ClassCastException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T readObj(InputStream in) throws JarvisException, ClassCastException {
        ObjectInputStream ois;
        try {
            ois = in instanceof ObjectInputStream ? (ObjectInputStream) in : new ObjectInputStream(in);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new JarvisException(e);
        }
    }

    /**
     * write content to the output stream
     *
     * @param out           output stream
     * @param isCloseStream whether to close the stream, if the stream has subsequent operations, it will not be closed
     * @param contents      what is written
     * @throws JarvisException throw JarvisException
     */
    public static void writeObjects(OutputStream out, boolean isCloseStream, Serializable... contents) throws JarvisException {
        ObjectOutputStream osw = null;
        try {
            osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
            for (Object content : contents) {
                if (content != null) {
                    osw.writeObject(content);
                }
            }
            osw.flush();
        } catch (IOException e) {
            throw new JarvisException(e);
        } finally {
            if (isCloseStream) {
                close(osw);
            }
        }
    }

    /**
     * write content to the output stream, and return byte array
     *
     * @param out      output stream
     * @param contents what is written
     * @return byte array
     * @throws JarvisException throw JarvisException
     */
    public static byte[] writeObjects(ByteArrayOutputStream out, Serializable... contents) throws JarvisException {
        Assert.notNull(out, "out must not be null!");
        try {
            writeObjects(out, false, contents);
            return out.toByteArray();
        } finally {
            close(out);
        }
    }

    /**
     * close
     * will not throw an exception
     *
     * @param closeable object being closed
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
