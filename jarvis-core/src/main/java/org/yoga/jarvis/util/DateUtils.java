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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description: Date utils
 * @Author: yoga
 * @Date: 2022/6/5 20:39
 */
public class DateUtils {

    /**
     * default zoneId
     */
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    /**
     * default zone offset
     */
    private static final ZoneOffset DEFAULT_ZONE_OFFSET = DEFAULT_ZONE_ID.getRules().getOffset(Instant.now());

    /**
     * {@code Date} format as a string with pattern
     *
     * @param date    {@code Date}, not null
     * @param pattern pattern, not blank
     * @return {@code String}
     */
    public static String format(Date date, String pattern) {
        Assert.notNull(date, "date must not be null!");
        Assert.notBlank(pattern, "pattern must not be blank!");
        return formatLocalDateTime(toLocalDateTime(date), pattern);
    }

    /**
     * {@code LocalDateTime} format as a string with pattern
     *
     * @param localDateTime {@code LocalDateTime}, not null
     * @param pattern       pattern, not blank
     * @return {@code String}
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        Assert.notNull(localDateTime, "localDateTime must not be null!");
        Assert.notBlank(pattern, "pattern must not be blank!");
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Get the {@code LocalDateTime} from a Date
     *
     * @param date {@code Date}, not null
     * @return {@code LocalDateTime}
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Assert.notNull(date, "date must not be null!");
        return toZonedDateTime(date).toLocalDateTime();
    }

    /**
     * Get the {@code ZonedDateTime} from a Date
     *
     * @param date {@code Date}, not null
     * @return {@code ZonedDateTime} {@link DateUtils#DEFAULT_ZONE_ID}
     */
    public static ZonedDateTime toZonedDateTime(Date date) {
        Assert.notNull(date, "date must not be null!");
        return date.toInstant().atZone(DEFAULT_ZONE_ID);
    }

    /**
     * get {@code LocalDateTime} from seconds
     *
     * @param seconds seconds, Greater than 0
     * @return {@code LocalDateTime}
     */
    public static LocalDateTime fromSeconds(long seconds) {
        Assert.isTrue(seconds > 0, "seconds must > 0");
        return LocalDateTime.ofEpochSecond(seconds, 0, DEFAULT_ZONE_OFFSET);
    }

    /**
     * get {@code LocalDateTime} from milliseconds
     *
     * @param millSeconds millSeconds, Greater than 0
     * @return {@code LocalDateTime}
     */
    public static LocalDateTime fromMillSeconds(long millSeconds) {
        Assert.isTrue(millSeconds > 0, "millSeconds must > 0");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millSeconds), DEFAULT_ZONE_ID);
    }

    /**
     * get timestamp from {@code LocalDateTime}
     *
     * @param localDateTime {@code LocalDateTime}, not null
     * @return {@code long} timestamp (ms)
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime must not be null!");
        return localDateTime.toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli();
    }

}
