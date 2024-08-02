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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoga.jarvis.constant.DateFormatConstant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Description: Json Utils
 * @Author: yoga
 * @Date: 2024/8/1 16:32
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // serialize all fields
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // cancel the conversion to timestamps
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // ignore the error of converting empty bean to json
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // set the date format
        objectMapper.setDateFormat(new SimpleDateFormat(DateFormatConstant.FMT_YMD_HMS));
        // ignore properties that don't exist in java objects
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * json string parse object
     *
     * @param jsonStr json string
     * @param clazz   class of object
     * @param <T>     generics
     * @return object
     */
    public static <T> T parseObj(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            logger.error("json string parse object fail", e);
        }
        return null;
    }

    /**
     * json string parse object
     *
     * @param jsonStr   json string
     * @param reference type reference
     * @param <T>       generics
     * @return object
     */
    public static <T> T parseObj(String jsonStr, TypeReference<T> reference) {
        try {
            return objectMapper.readValue(jsonStr, reference);
        } catch (JsonProcessingException e) {
            logger.error("json string parse object fail", e);
        }
        return null;
    }

    /**
     * json string (from file) parse object
     *
     * @param file  file
     * @param clazz class of object
     * @param <T>   generics
     * @return object
     */
    public static <T> T parseObj(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            logger.error("failed to read JSON string from file to object", e);
        }
        return null;
    }

    //将json数组字符串转为指定对象List列表或者Map集合


    /**
     * object to json string
     *
     * @param obj object
     * @return json string
     */
    public static String toJSONStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("object to json string fail", e);
        }
        return null;
    }



}
