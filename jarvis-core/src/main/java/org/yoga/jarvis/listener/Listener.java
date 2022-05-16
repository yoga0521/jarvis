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

package org.yoga.jarvis.listener;

/**
 * @Description: listener
 * @Author: yoga
 * @Date: 2022/5/13 13:56
 */
public interface Listener {

    /**
     * When event processing is complete
     */
    void onComplete();

    /**
     * When cancel event processing
     *
     * @param event {@link org.yoga.jarvis.listener.Event}
     */
    void onCancel(Event event);

    /**
     * There was an error when the event was processed
     *
     * @param event {@link org.yoga.jarvis.listener.Event}
     */
    void onError(Event event);
}
