package com.chilun.deneng.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @auther 齿轮
 * @create 2023-07-05-14:29
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(HttpServletResponse.SC_OK,"操作成功"),
    FAILURE(HttpServletResponse.SC_BAD_REQUEST,"业务操作异常"),
    UN_AUTHOR(HttpServletResponse.SC_UNAUTHORIZED,"请求无权限"),
    NOT_FOUNT(HttpServletResponse.SC_NOT_FOUND,"资源未找到"),
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST,"请求消息无法读取"),
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"请求的方法不支持"),
    INNER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"服务器内部错误"),
    PRAM_MISS(HttpServletResponse.SC_BAD_REQUEST,"请求参数丢失");

    final int code;
    final String msg;

}
