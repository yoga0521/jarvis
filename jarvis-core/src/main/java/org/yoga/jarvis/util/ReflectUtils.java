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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: ReflectUtils
 * @Author: yoga
 * @Date: 2022/8/20 23:09
 */
public class ReflectUtils {

    /**
     * get constructors
     *
     * @param clazz {@code Class}
     * @return constructors
     */
    public static Constructor<?>[] getConstructors(Class<?> clazz) {
        Assert.notNull(clazz, "clazz must not be null!");
        return clazz.getDeclaredConstructors();
    }

    /**
     * get constructors
     *
     * @param obj {@code Object}
     * @return constructors
     */
    public static Constructor<?>[] getConstructors(Object obj) {
        Assert.notNull(obj, "obj must not be null!");

        return getConstructors(obj.getClass());
    }

    /**
     * get fields
     *
     * @param clazz {@code Class}
     * @return fields
     */
    public static Field[] getFields(Class<?> clazz) {
        Assert.notNull(clazz, "clazz must not be null!");
        return clazz.getDeclaredFields();
    }

    /**
     * get fields
     *
     * @param obj {@code Object}
     * @return fields
     */
    public static Field[] getFields(Object obj) {
        Assert.notNull(obj, "obj must not be null!");

        return getFields(obj.getClass());
    }

    /**
     * get field
     *
     * @param clazz     {@code Class}
     * @param fieldName field name
     * @return field
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Assert.notNull(clazz, "clazz must not be null!");
        Assert.notBlank(fieldName, "fieldName must not be blank!");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * get field
     *
     * @param obj       {@code Object}
     * @param fieldName field name
     * @return field
     */
    public static Field getField(Object obj, String fieldName) {
        Assert.notNull(obj, "obj must not be null!");
        Assert.notBlank(fieldName, "fieldName must not be blank!");

        return getField(obj.getClass(), fieldName);
    }

    /**
     * get field value
     *
     * @param obj   obj {@code Object}
     * @param field field {@code Field}
     * @return field value
     * @throws JarvisException throw JarvisException
     */
    public static Object getFieldValue(Object obj, Field field) throws JarvisException {
        Assert.notNull(obj, "obj must not be null!");
        Assert.notNull(field, "field must not be null!");

        // field accessible init value
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) {
            field.setAccessible(true);
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new JarvisException(e);
        } finally {
            // set field accessible init value
            if (!isFieldAccessible) {
                field.setAccessible(false);
            }
        }
    }

    /**
     * get field value
     *
     * @param obj       obj {@code Object}
     * @param fieldName fieldName
     * @return field value
     * @throws JarvisException throw JarvisException
     */
    public static Object getFieldValue(Object obj, String fieldName) throws JarvisException {
        Assert.notNull(obj, "obj must not be null!");
        Assert.notBlank(fieldName, "fieldName must not be blank!");

        Field field = getField(obj, fieldName);
        return null == field ? null : getFieldValue(obj, field);
    }

    /**
     * does the field exist
     *
     * @param clazz     clazz {@code Class}
     * @param fieldName fieldName
     * @return does the field exist
     */
    public static boolean isFieldExist(Class<?> clazz, String fieldName) {
        Assert.notNull(clazz, "clazz must not be null!");
        Assert.notBlank(fieldName, "fieldName must not be blank!");

        for (Field field : getFields(clazz)) {
            if (fieldName.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * does the field exist
     *
     * @param obj       obj {@code Object}
     * @param fieldName fieldName
     * @return does the field exist
     */
    public static boolean isFieldExist(Object obj, String fieldName) {
        Assert.notNull(obj, "obj must not be null!");
        Assert.notBlank(fieldName, "fieldName must not be blank!");

        return isFieldExist(obj.getClass(), fieldName);
    }

    /**
     * invoke method
     *
     * @param obj    obj {@code Object}
     * @param method method
     * @param args   params
     * @return invoke result
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj, Method method, Object... args) {
        Assert.notNull(obj, "obj must not be null!");
        Assert.notNull(method, "method must not be null!");

        // method accessible init value
        boolean isMethodAccessible = method.isAccessible();
        // set method accessible
        if (!isMethodAccessible) {
            method.setAccessible(true);
        }

        // when the method is the default method, you need to use the handle to execute
        if (method.isDefault()) {
            // todo
        }
        final Object[] actualArgs;
        if (method.getParameterCount() > 0) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            actualArgs = new Object[parameterTypes.length];
            // ignore redundant parameters
            if (null != args) {
                // throw an exception if there are not enough args
                if (args.length < parameterTypes.length) {
                    throw new JarvisException("the number of args is less than the default number of parameters for the method");
                }
                for (int i = 0; i < actualArgs.length; i++) {
                    // the arg is null, but the target parameter type is a primitive type, use default values for primitive types
                    if (args[i] == null && parameterTypes[i].isPrimitive()) {
                        actualArgs[i] = ObjectUtils.getPrimitiveDefaultValue(parameterTypes[i]);
                    } else {
                        actualArgs[i] = args[i];
                    }
                }
            }
        } else {
            actualArgs = args;
        }

        try {
            return (T) method.invoke(obj, actualArgs);
        } catch (SecurityException | InvocationTargetException | IllegalAccessException e) {
            throw new JarvisException(e);
        } finally {
            // set method accessible init value
            if (!isMethodAccessible) {
                method.setAccessible(false);
            }
        }
    }

}
