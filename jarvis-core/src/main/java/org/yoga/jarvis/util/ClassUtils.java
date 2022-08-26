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

import org.yoga.jarvis.constant.BasicType;

/**
 * @Description: ClassUtils
 * @Author: yoga
 * @Date: 2022/8/24 16:57
 */
public class ClassUtils {

    /**
     * get class array
     *
     * @param objects object array
     * @return class array
     */
    public static Class<?>[] getClasses(Object... objects) {
        Assert.notNull(objects, "objects must not be null!");

        if (objects.length == 0) {
            return new Class<?>[0];
        }

        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i] == null ? Object.class : objects[i].getClass();
        }
        return classes;
    }

    /**
     * whether it is a wrapper
     *
     * @param clazz clazz {@code Class}
     * @return whether it is a wrapper
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return BasicType.WRAPPER_BASIC_TYPE_MAP.containsKey(clazz);
    }

    /**
     * whether it is a basic type (including wrapper and primitive)
     *
     * @param clazz clazz {@code Class}
     * @return whether it is a basic type
     */
    public static boolean isBasicType(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }
}
