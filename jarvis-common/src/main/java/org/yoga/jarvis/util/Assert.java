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

import java.util.Collection;
import java.util.Map;

/**
 * @Description: Assert
 * @Author: yoga
 * @Date: 2022/5/16 13:25
 */
public class Assert {

    /**
     * Assert a boolean expression
     *
     * @param expression boolean expression
     * @param message    exception message to use when it fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     */
    public static void isTrue(final boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert an {@code object} is {@code null}
     *
     * <pre class="code">
     * Assert.isNull(obj, "The obj must be null");
     * </pre>
     *
     * @param object  the object to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code object} is not {@code null}
     */
    public static <T> void isNull(final T object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert an {@code object} is not {@code null}
     *
     * <pre class="code">
     * Assert.notNull(obj, "The obj must not be null");
     * </pre>
     *
     * @param object  the object to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code object} is {@code null}
     */
    public static <T> void notNull(final T object, String message) {
        if (null == object) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code cs} is empty
     *
     * <pre class="code">
     * Assert.isEmpty(str, "The str must be empty");
     * </pre>
     *
     * @param cs      the cs to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code cs} is not empty
     * @see StringUtils#isNotEmpty(CharSequence)
     */
    public static void isEmpty(final CharSequence cs, String message) {
        if (StringUtils.isNotEmpty(cs)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert an object array is empty
     *
     * <pre class="code">
     * Assert.isEmpty(array, "The array must be empty");
     * </pre>
     *
     * @param array   the array to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code array} is not empty
     */
    public static void isEmpty(final Object[] array, String message) {
        if (array != null && array.length != 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a char array is empty
     *
     * <pre class="code">
     * Assert.isEmpty(array, "The array must be empty");
     * </pre>
     *
     * @param array   the array to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code array} is not empty
     */
    public static void isEmpty(final char[] array, String message) {
        if (array != null && array.length != 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code collection} is empty
     *
     * <pre class="code">
     * Assert.isEmpty(collection, "The collection must be empty");
     * </pre>
     *
     * @param collection the collection to check, nullable
     * @param message    exception message to use when it fails
     * @throws IllegalArgumentException if the {@code collection} is not empty
     */
    public static void isEmpty(final Collection<?> collection, String message) {
        if (collection != null && collection.size() != 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code map} is empty
     *
     * <pre class="code">
     * Assert.isEmpty(map, "The map must be empty");
     * </pre>
     *
     * @param map     the map to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code map} is not empty
     */
    public static void isEmpty(final Map<?, ?> map, String message) {
        if (map != null && map.size() != 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code cs} is not empty
     *
     * <pre class="code">
     * Assert.notEmpty(str, "The str must not be empty");
     * </pre>
     *
     * @param cs      the cs to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code cs} is empty
     * @see StringUtils#isEmpty(CharSequence)
     */
    public static void notEmpty(final CharSequence cs, String message) {
        if (StringUtils.isEmpty(cs)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert an object array is not empty
     *
     * <pre class="code">
     * Assert.notEmpty(array, "The array must not be empty");
     * </pre>
     *
     * @param array   the array to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code array} is empty
     */
    public static void notEmpty(final Object[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a char array is not empty
     *
     * <pre class="code">
     * Assert.notEmpty(array, "The array must not be empty");
     * </pre>
     *
     * @param array   the array to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code array} is empty
     */
    public static void notEmpty(final char[] array, String message) {
        if (null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code collection} is not empty
     *
     * <pre class="code">
     * Assert.notEmpty(collection, "The collection must not be empty");
     * </pre>
     *
     * @param collection the collection to check, nullable
     * @param message    exception message to use when it fails
     * @throws IllegalArgumentException if the {@code collection} is empty
     */
    public static void notEmpty(final Collection<?> collection, String message) {
        if (null == collection || collection.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code map} is not empty
     *
     * <pre class="code">
     * Assert.notEmpty(map, "The map must not be empty");
     * </pre>
     *
     * @param map     the map to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code map} is empty
     */
    public static void notEmpty(final Map<?, ?> map, String message) {
        if (null == map || map.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a {@code CharSequence} is not {@code null} and length greater 0 and not whitespace only
     *
     * <pre class="code">
     * Assert.notBlank(str, "The obj must not be null");
     * </pre>
     *
     * @param cs      the {@code CharSequence} to check, nullable
     * @param message exception message to use when it fails
     * @throws IllegalArgumentException if the {@code CharSequence} is {@code null} or has length 0 or has whitespace only
     * @see StringUtils#isBlank(CharSequence)
     */
    public static void notBlank(final CharSequence cs, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new IllegalArgumentException(message);
        }
    }
}
