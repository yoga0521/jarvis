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

package org.yoga.jarvis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yoga.jarvis.OssHandler;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.bean.Result;
import org.yoga.jarvis.exception.JarvisException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: OssTestController
 * @Author: yoga
 * @Date: 2022/6/5 20:39
 */
@RestController
@RequestMapping("oss")
public class OssTestController {

    final OssHandler ossHandlerAdapter;

    public OssTestController(OssHandler ossHandlerAdapter) {
        this.ossHandlerAdapter = ossHandlerAdapter;
    }

    @PostMapping("upload")
    public Result<OssResourceDTO> testUpload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new JarvisException("file must not be null!");
        }
        try (InputStream input = file.getInputStream()) {
            OssResourceDTO ret = ossHandlerAdapter.upload(input, file.getOriginalFilename(), file.getSize(),
                    null, true, false);
            return Result.success(ret);
        } catch (IOException e) {
            throw new JarvisException("file upload fail!");
        }
    }

}
