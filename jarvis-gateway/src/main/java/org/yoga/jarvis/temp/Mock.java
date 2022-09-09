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

package org.yoga.jarvis.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: mock infos
 * @Author: yoga
 * @Date: 2022/9/9 10:18
 */
public class Mock {

    /**
     * mock service url mapping
     *
     * @return service url mapping
     */
    public static Map<String, List<String>> mockServiceUrlMap() {
        List<String> userUrls = new ArrayList<>();
        userUrls.add("http://127.0.0.1:8876");
        userUrls.add("http://127.0.0.1:8877");
        List<String> shopUrls = new ArrayList<>();
        shopUrls.add("http://127.0.0.1:7766");
        Map<String, List<String>> urlMap = new ConcurrentHashMap<>(8);
        urlMap.put("user", userUrls);
        urlMap.put("shop", shopUrls);
        return urlMap;
    }
}
