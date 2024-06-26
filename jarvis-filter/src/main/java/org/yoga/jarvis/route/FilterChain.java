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

package org.yoga.jarvis.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Filter Chain
 * @Author: yoga
 * @Date: 2024/5/27 17:27
 */
public class FilterChain {

    private List<RoutingFilter> filters = new ArrayList<>();

    public void addFilter(RoutingFilter filter) {
        filters.add(filter);
    }

    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        for (RoutingFilter filter : filters) {
            filter.filter(request, response);
        }
        return true;
    }
}
