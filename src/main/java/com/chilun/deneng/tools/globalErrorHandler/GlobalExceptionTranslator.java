package com.chilun.deneng.tools.globalErrorHandler;

import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionTranslator {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleError(MissingServletRequestParameterException exception) {
        //日志：。。。
        String message = String.format("未传递请求参数:%s", exception.getParameterName());
        return new BaseResponse(message, ResultCode.PRAM_MISS);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleError(Exception exception) {
        return new BaseResponse(exception.getMessage(), ResultCode.FAILURE);
    }
}