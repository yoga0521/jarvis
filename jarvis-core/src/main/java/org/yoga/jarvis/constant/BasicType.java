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

/**
 * @Description: Basic Type
 * @Author: yoga
 * @Date: 2022/8/24 17:54
 */
public enum BasicType {
    /**
     * BOOLEAN Type
     */
    BOOLEAN(Boolean.TYPE, Boolean.class, false),
    /**
     * CHAR Type
     */
    CHAR(Character.TYPE, Character.class, (char) 0),
    /**
     * BYTE Type
     */
    BYTE(Byte.TYPE, Byte.class, (byte) 0),
    /**
     * SHORT Type
     */
    SHORT(Short.TYPE, Short.class, (short) 0),
    /**
     * INTEGER Type
     */
    INTEGER(Integer.TYPE, Integer.class, 0),
    /**
     * LONG Type
     */
    LONG(Long.TYPE, Long.class, 0L),
    /**
     * FLOAT Type
     */
    FLOAT(Float.TYPE, Float.class, 0f),
    /**
     * DOUBLE Type
     */
    DOUBLE(Double.TYPE, Double.class, 0d);

    /**
     * primitive type class
     */
    private final Class<?> primitiveClazz;

    /**
     * wrapper type class
     */
    private final Class<?> wrapperClazz;

    /**
     * default value when null
     */
    private final Object defaultValue;

    BasicType(Class<?> primitiveClazz, Class<?> wrapperClazz, Object defaultValue) {
        this.primitiveClazz = primitiveClazz;
        this.wrapperClazz = wrapperClazz;
        this.defaultValue = defaultValue;
    }

    public Class<?> getPrimitiveClazz() {
        return primitiveClazz;
    }

    public Class<?> getWrapperClazz() {
        return wrapperClazz;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
