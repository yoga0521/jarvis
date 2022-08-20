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

import java.lang.reflect.Field;

/**
 * @Description: ReflectUtils
 * @Author: yoga
 * @Date: 2022/8/20 23:09
 */
public class ReflectUtils {

    /**
     * get field value
     *
     * @param obj       obj {@code Object}
     * @param fieldName fieldName
     * @return field value
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Assert.notBlank(fieldName, "fieldName must not be blank!");
        if (obj == null) {
            return null;
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                try {
                    return field.get(obj);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new JarvisException(e);
                }
            }
        }
        return null;
    }

    /**
     * does the field exist
     *
     * @param obj       obj {@code Object}
     * @param fieldName fieldName
     * @return does the field exist
     */
    public static boolean isFieldExist(Object obj, String fieldName) {
        Assert.notBlank(fieldName, "fieldName must not be blank!");
        if (obj == null) {
            return false;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

}
