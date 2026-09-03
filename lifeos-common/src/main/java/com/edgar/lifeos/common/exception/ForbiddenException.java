package com.edgar.lifeos.common.exception;

import com.edgar.lifeos.common.api.ErrorCode;

/**
 * 无写权限 / 越权访问，对应 403。
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}