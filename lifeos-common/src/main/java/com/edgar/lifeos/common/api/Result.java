package com.edgar.lifeos.common.api;

/**
 * 统一响应包装。
 *
 * <p>所有私人接口返回格式：{@code {code, message, data, requestId}}，
 * code = 0 表示成功。错误码见 {@link ErrorCode}。</p>
 */
public record Result<T>(int code, String message, T data, String requestId) {

    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data, null);
    }

    public static <T> Result<T> success(T data, String requestId) {
        return new Result<>(0, "success", data, requestId);
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message, null, null);
    }

    public static <T> Result<T> failure(int code, String message, String requestId) {
        return new Result<>(code, message, null, requestId);
    }

    public Result<T> withRequestId(String requestId) {
        return requestId == null ? this : new Result<>(code, message, data, requestId);
    }
}