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

package org.yoga.jarvis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.yoga.jarvis.constant.DateFormatConstant;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description: JacksonConfig
 * @Author: yoga
 * @Date: 2022/8/9 18:18
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = Jackson2ObjectMapperBuilder
                .json()
                // If the accessor of the conversion class is not found, it defaults to an empty object
                .failOnEmptyBeans(false)
                // Ignore unknown properties
                .failOnUnknownProperties(false)
                // Properties with a non-null value will only contain properties
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                // set time zone
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .featuresToEnable(MapperFeature.USE_STD_BEAN_NAMING)
                .build();

        SimpleModule simpleModule = new SimpleModule();
        // Loss of precision after more than 16 bits
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance)
                // date Serializer Deserializer
                .addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DateFormatConstant.FMT_YMD_HMS)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                        new SimpleDateFormat(DateFormatConstant.FMT_YMD_HMS), DateFormatConstant.FMT_YMD_HMS))
                .addSerializer(new DateSerializer(false, new SimpleDateFormat(DateFormatConstant.FMT_YMD_HM)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                        new SimpleDateFormat(DateFormatConstant.FMT_YMD_HM), DateFormatConstant.FMT_YMD_HM))
                .addSerializer(new DateSerializer(false, new SimpleDateFormat(DateFormatConstant.FMT_YMD)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                        new SimpleDateFormat(DateFormatConstant.FMT_YMD), DateFormatConstant.FMT_YMD))
                .addSerializer(new DateSerializer(false, new SimpleDateFormat(DateFormatConstant.FMT_YM)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                        new SimpleDateFormat(DateFormatConstant.FMT_YM), DateFormatConstant.FMT_YM));

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDate Serializer
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateFormatConstant.DATE_FORMATTER));
        // LocalDate Deserializer
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateFormatConstant.DATE_FORMATTER));
        // LocalDateTime Serializer
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatConstant.DATE_TIME_FORMATTER));
        // LocalDateTime Deserializer
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatConstant.DATE_TIME_FORMATTER));

        mapper.registerModule(simpleModule).registerModule(javaTimeModule).registerModule(new Jdk8Module());
        return mapper;
    }
}
