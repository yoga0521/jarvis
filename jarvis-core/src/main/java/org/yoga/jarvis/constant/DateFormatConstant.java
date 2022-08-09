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

import java.time.format.DateTimeFormatter;

/**
 * @Description: DateFormatConstant
 * @Author: yoga
 * @Date: 2022/8/9 18:20
 */
public class DateFormatConstant {

    /**
     * year month
     */
    public static final String FMT_YM = "yyyy-MM";

    /**
     * year month day
     */
    public static final String FMT_YMD = "yyyy-MM-dd";

    /**
     * year month day time
     */
    public static final String FMT_YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * year month day time
     */
    public static final String FMT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * date format
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FMT_YMD);

    /**
     * date time format
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FMT_YMD_HMS);

}
