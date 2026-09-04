package com.edgar.lifeos.common.exception;

import com.edgar.lifeos.common.api.ErrorCode;
import lombok.Getter;

/**
 * 业务异常基类。所有业务域异常都继承它。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(ErrorCode.BUSINESS_ERROR, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}