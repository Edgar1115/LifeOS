package com.edgar.lifeos.common.api;

/**
 * 统一错误码。
 *
 * <p>业务模块可继承扩展（如 {@code capture.Errors}），但基础错误码集中在 common。</p>
 *
 * <p>分段规划：<br>
 * 0xxxx —— 通用成功/失败<br>
 * 4xxxx —— 客户端错误（参数、认证、权限、资源不存在、冲突）<br>
 * 5xxxx —— 服务端错误（业务处理异常、内部异常）</p>
 */
public final class ErrorCode {

    private ErrorCode() {
    }

    /** 成功 */
    public static final int SUCCESS = 0;

    /** 参数错误 */
    public static final int BAD_REQUEST = 40000;

    /** 未认证 */
    public static final int UNAUTHORIZED = 40100;

    /** 无权限（认证但越权） */
    public static final int FORBIDDEN = 40300;

    /** 资源不存在（对资源存在的探测应返回 404） */
    public static final int NOT_FOUND = 40400;

    /** 状态冲突，如重复创建、并发修改 */
    public static final int CONFLICT = 40900;

    /** 通用业务错误 */
    public static final int BUSINESS_ERROR = 50001;

    /** 系统内部错误 */
    public static final int INTERNAL_ERROR = 50000;
}