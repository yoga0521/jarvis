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

package org.yoga.jarvis.bean.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.yoga.jarvis.bean.Result;
import org.yoga.jarvis.constant.ResponseCode;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.ArrayUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @Description: 全局异常处理类
 * @Author: yoga
 * @Date: 2024/8/26 11:46
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*------  参数校验错误处理  -------*/
    @ExceptionHandler(value = {BindException.class})
    public Result<Void> bindExceptionHandler(BindException e) {
        log.error("参数绑定失败错误", e);
        String errMsg = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        return Result.fail(ResponseCode.VALIDATE_FAILED.getCode(), errMsg);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数校验失败错误", e);
        String errMsg = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        return Result.fail(ResponseCode.VALIDATE_FAILED.getCode(), errMsg);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result<Void> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("参数校验失败错误", e);
        String errMsg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
        return Result.fail(ResponseCode.VALIDATE_FAILED.getCode(), errMsg);
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public Result<Void> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        log.error("上传文件大小超过限制错误", e);
        return Result.fail(ResponseCode.VALIDATE_FAILED.getCode(), "上传文件大小超过限制" + (e.getMaxUploadSize() >> 20) + "MB");
    }

    @ExceptionHandler(value = {RestClientException.class})
    public Result<Void> restClientExceptionHandler(RestClientException e) {
        log.error("restHttp请求错误", e);
        return Result.fail("远程同步外部平台信息异常，" + e.getMessage());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public Result<Void> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        String defaultMsg = "http请求方式不支持 [" + e.getMethod() + "]";
        if (ArrayUtils.isNotEmpty(e.getSupportedMethods())) {
            defaultMsg += " ，请用 [" + String.join(",", e.getSupportedMethods()) + "] 方式请求";
        }
        log.error(defaultMsg, e);
        return Result.fail(defaultMsg);
    }

    /*------  Jarvis异常处理  -------*/
    @ExceptionHandler(value = {JarvisException.class})
    public Result<Void> jarvisExceptionHandler(JarvisException e) {
        log.error("Jarvis异常，code:{}", e.getCode(), e);
        return Result.fail(e.getMessage());
    }

    /*------  未捕获异常处理  -------*/
    @ExceptionHandler(value = {Exception.class})
    public Result<Void> exceptionHandler(Exception e) {
        log.error("系统未捕获异常", e);
        return Result.fail(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }
}
