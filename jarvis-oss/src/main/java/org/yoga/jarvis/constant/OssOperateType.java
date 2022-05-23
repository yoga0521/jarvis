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

package org.yoga.jarvis.constant;

/**
 * @Description: oss operate type
 * @Author: yoga
 * @Date: 2022/5/20 10:58
 */
public enum OssOperateType {

    /**
     * upload
     */
    upload("uploaded", "uploading", "上传"),

    /**
     * download
     */
    download("downloaded", "downloading", "下载");

    /**
     * simple past
     */
    private final String simplePast;

    /**
     * present continuous
     */
    private final String presentContinuous;

    /**
     * Chinese name
     */
    private final String chineseName;

    OssOperateType(String simplePast, String presentContinuous, String chineseName) {
        this.simplePast = simplePast;
        this.presentContinuous = presentContinuous;
        this.chineseName = chineseName;
    }

    public String getSimplePast() {
        return simplePast;
    }

    public String getPresentContinuous() {
        return presentContinuous;
    }

    public String getChineseName() {
        return chineseName;
    }
}
