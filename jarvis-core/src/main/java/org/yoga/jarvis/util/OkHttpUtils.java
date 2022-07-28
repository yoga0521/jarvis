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

import okhttp3.*;
import org.yoga.jarvis.exception.JarvisException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: OkHttpUtils
 * @Author yoga
 * @Date 2022-05-10 17:40
 */
public class OkHttpUtils {

    private static final OkHttpClient OKHTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    /**
     * OKHTTP  POST
     *
     * @param reqUrl       request url
     * @param header       header
     * @param paramJsonStr param json str
     * @return result
     */
    public static String okHttpPost(String reqUrl, Map<String, String> header, String paramJsonStr) {
        RequestBody body = RequestBody.create(paramJsonStr, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder builder = new Request.Builder().url(reqUrl);
        if (header != null && !header.isEmpty()) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.post(body).build();
        try {
            Response response = OKHTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new JarvisException("HTTP Request Fail URL: " + reqUrl, e);
        }
    }
}
