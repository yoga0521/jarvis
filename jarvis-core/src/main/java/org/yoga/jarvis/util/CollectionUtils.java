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
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * @Description: Collection Utils
 * @Author: yoga
 * @Date: 2022/5/16 16:12
 */
public class CollectionUtils {

    /**
     * Check an array is {@code null} or has length 0
     *
     * <pre class="code">
     * CollectionsUtils.isEmpty({@code null}) = true
     * CollectionsUtils.isEmpty(new ArrayList()) = true
     * CollectionsUtils.isEmpty(new ArrayList("Jarvis")) = false
     * </pre>
     *
     * @param collection the {@code Collection}
     * @return {@code true} if the {@code Collection} is {@code null} or has length 0
     */
    public static <T> boolean isEmpty(final Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * Check a {@code Collection} is not {@code null} and has size
     *
     * <pre class="code">
     * CollectionsUtils.isNotEmpty({@code null}) = false
     * CollectionsUtils.isNotEmpty(new ArrayList()) = false
     * CollectionsUtils.isNotEmpty(new ArrayList("Jarvis")) = true
     * CollectionsUtils.isNotEmpty(new ArrayList("Jarvis")) = true
     * </pre>
     *
     * @param collection the {@code Collection}
     * @return {@code true} if the {@code Collection} is not {@code null} and has size
     */
    public static <T> boolean isNotEmpty(final Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * Returns a stream consisting of the elements of this stream that match
     * the given predicate.
     *
     * @param filter     a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                   <a href="package-summary.html#Statelessness">stateless</a>
     *                   predicate to apply to each element to determine if it
     *                   should be included
     * @param downstream original stream
     * @return the new stream
     */
    public static <T, A, R> Collector<T, A, R> filtering(Predicate<? super T> filter, Collector<T, A, R> downstream) {
        BiConsumer<A, T> accumulator = downstream.accumulator();
        Set<Collector.Characteristics> characteristics = downstream.characteristics();
        return Collector.of(downstream.supplier(), (acc, t) -> {
                    if (filter.test(t)) {
                        accumulator.accept(acc, t);
                    }
                }, downstream.combiner(), downstream.finisher(),
                characteristics.toArray(new Collector.Characteristics[0]));
    }
}
