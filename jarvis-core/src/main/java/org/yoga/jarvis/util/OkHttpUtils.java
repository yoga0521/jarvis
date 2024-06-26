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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * OKHTTP  POST
     *
     * @param reqUrl       request url
     * @param header       header
     * @param paramJsonStr param json str
     * @return result
     */
    public static String post(String reqUrl, Map<String, String> header, String paramJsonStr) {
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

    /**
     * OKHTTP  execute
     *
     * @param url          request url
     * @param header       header
     * @param method       method
     * @param paramJsonStr param json str
     * @return result
     */
    public static String execute(String url, Map<String, String> header, String method, String paramJsonStr) {

        Request.Builder builder = new Request.Builder().url(url);
        if (header != null && !header.isEmpty()) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.method(method, StringUtils.isNotBlank(paramJsonStr) ? RequestBody.create(paramJsonStr, MediaType.parse("application/json; charset=utf-8")) : null).build();
        try {
            Response response = OKHTTP_CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            throw new JarvisException("HTTP Request Fail URL: " + url, e);
        }
    }
}
