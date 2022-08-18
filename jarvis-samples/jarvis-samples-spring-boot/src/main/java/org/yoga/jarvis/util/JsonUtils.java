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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yoga.jarvis.exception.JarvisException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: json工具类
 * @Author: yoga
 * @Date: 2022/8/16 18:05
 */
@Slf4j
@Component
public class JsonUtils {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    /**
     * json字符串转为对象
     *
     * @param jsonStr   json字符串
     * @param valueType 对象类型
     * @param <T>       泛型
     * @return 对象
     */
    public static <T> T parseObj(String jsonStr, Class<T> valueType) {
        return parseObj(jsonStr, valueType, "json字符串转对象失败");
    }

    /**
     * json字符串转为对象
     *
     * @param jsonStr       json字符串
     * @param valueType     对象类型
     * @param covertFailMsg 转换失败提示信息
     * @param <T>           泛型
     * @return 对象
     */
    public static <T> T parseObj(String jsonStr, Class<T> valueType, String covertFailMsg) {
        Assert.notNull(jsonStr, "json字符串为空");
        Assert.notNull(valueType, "转换类型为空");
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (JsonProcessingException e) {
            throw new JarvisException(covertFailMsg);
        }
    }

    /**
     * json字符串转为对象
     *
     * @param jsonStr      json字符串
     * @param valueTypeRef 对象类型
     * @param <T>          泛型
     * @return 对象
     */
    public static <T> T parseObj(String jsonStr, TypeReference<T> valueTypeRef) {
        return parseObj(jsonStr, valueTypeRef, "json字符串转对象失败");
    }

    /**
     * json字符串转为对象
     *
     * @param jsonStr       json字符串
     * @param valueTypeRef  对象类型
     * @param covertFailMsg 转换失败提示信息
     * @param <T>           泛型
     * @return 对象
     */
    public static <T> T parseObj(String jsonStr, TypeReference<T> valueTypeRef, String covertFailMsg) {
        Assert.notNull(jsonStr, "json字符串为空");
        Assert.notNull(valueTypeRef, "转换类型为空");
        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new JarvisException(covertFailMsg);
        }
    }

    /**
     * 对象转为json字符串
     *
     * @param obj 待转换对象
     * @return json字符串
     */
    public static String toJsonStr(Object obj) {
        return toJsonStr(obj, "对象转json字符串失败");
    }

    /**
     * 对象转为json字符串
     *
     * @param obj           待转换对象
     * @param covertFailMsg 转换失败提示信息
     * @return json字符串
     */
    public static String toJsonStr(Object obj, String covertFailMsg) {
        Assert.notNull(obj, "待转换对象为空");
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JarvisException(covertFailMsg);
        }
    }

    /**
     * 深拷贝
     *
     * @param obj       待拷贝对象
     * @param valueType 拷贝后的对象类型
     * @param <T>       泛型
     * @return 拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, Class<T> valueType) {
        return parseObj(toJsonStr(obj), valueType);
    }

    /**
     * 深拷贝
     *
     * @param obj           待拷贝对象
     * @param valueType     拷贝后的对象类型
     * @param covertFailMsg 拷贝失败提示消息
     * @param <T>           泛型
     * @return 拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, Class<T> valueType, String covertFailMsg) {
        return parseObj(toJsonStr(obj, covertFailMsg), valueType, covertFailMsg);
    }

    /**
     * 深拷贝
     *
     * @param obj          待拷贝对象
     * @param valueTypeRef 拷贝后的对象类型
     * @param <T>          泛型
     * @return 拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, TypeReference<T> valueTypeRef) {
        return parseObj(toJsonStr(obj), valueTypeRef);
    }

    /**
     * 深拷贝
     *
     * @param obj           待拷贝对象
     * @param valueTypeRef  拷贝后的对象类型
     * @param covertFailMsg 拷贝失败提示消息
     * @param <T>           泛型
     * @return 拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, TypeReference<T> valueTypeRef, String covertFailMsg) {
        return parseObj(toJsonStr(obj, covertFailMsg), valueTypeRef, covertFailMsg);
    }

    /**
     * 转为排序后的map
     *
     * @param obj 待转换对象，满足k v格式
     * @return 排序后的map
     */
    public static Map<String, Object> toSortedMap(Object obj) {
        Map<String, Object> objMap = deepCopy(obj, new TypeReference<Map<String, Object>>() {
        });
        return objMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
    }

}
