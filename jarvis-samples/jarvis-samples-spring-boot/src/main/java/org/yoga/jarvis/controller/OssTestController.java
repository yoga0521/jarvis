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

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yoga.jarvis.OssHandler;
import org.yoga.jarvis.bean.OssResourceDTO;
import org.yoga.jarvis.bean.Result;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.Assert;

import java.io.ByteArrayInputStream;
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

    /**
     * upload file
     *
     * @param file file
     * @return upload result
     */
    @PostMapping("upload")
    public Result<OssResourceDTO> testUpload(MultipartFile file) {
        if (null == file || file.isEmpty()) {
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

    /**
     * download file
     *
     * @param objectName the name of oss object
     * @param fileName   the name of downloaded file
     * @return resource of file
     */
    @GetMapping("download")
    public ResponseEntity<InputStreamResource> testDownload(String objectName, String fileName) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        Assert.notBlank(fileName, "fileName must not be blank!");
        byte[] bytes = ossHandlerAdapter.download(objectName);

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        try (InputStream input = new ByteArrayInputStream(bytes)) {
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(bytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(input));
        } catch (IOException e) {
            throw new JarvisException("file download fail!");
        }
    }

    /**
     * generate oss temp url
     *
     * @param objectName the name of oss object
     * @return oss temp url
     */
    @GetMapping("generateOssUrl")
    public Result<String> testGenerateOssUrl(String objectName) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        return Result.success(ossHandlerAdapter.generateOssUrl(objectName, 5 * 60 * 1000L));
    }

    /**
     * delete file from oss
     *
     * @param objectName the name of oss object
     * @return delete result
     */
    @DeleteMapping("delete")
    public Result<Void> testDelete(String objectName) {
        Assert.notBlank(objectName, "objectName must not be blank!");
        ossHandlerAdapter.delete(objectName);
        return Result.success();
    }

}
