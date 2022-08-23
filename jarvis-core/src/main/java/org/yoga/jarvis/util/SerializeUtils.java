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

package org.yoga.jarvis.util;

import org.yoga.jarvis.exception.JarvisException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * @Description: SerializeUtils
 * @Author: yoga
 * @Date: 2022/8/23 16:39
 */
public class SerializeUtils {

    /**
     * cloning by copying the stream after serialize
     *
     * @param obj {@code Object}
     * @param <T> generics
     * @return cloned object
     * @throws JarvisException    throw JarvisException
     * @throws ClassCastException if the type convert fail, then throw ClassCastException
     */
    public static <T extends Serializable> T clone(T obj) {
        return deserialize(serialize(obj));
    }

    /**
     * serialize
     *
     * @param obj {@code Object}
     * @param <T> generics
     * @return byte array
     */
    public static <T extends Serializable> byte[] serialize(T obj) {
        return IOUtils.writeObjects(new ByteArrayOutputStream(), obj);
    }

    /**
     * deserialize
     *
     * @param bytes byte array
     * @param <T>   generics
     * @return obj {@code Object}
     */
    public static <T extends Serializable> T deserialize(byte[] bytes) {
        return IOUtils.readObj(new ByteArrayInputStream(bytes));
    }
}
