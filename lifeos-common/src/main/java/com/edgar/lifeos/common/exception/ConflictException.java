package com.edgar.lifeos.common.exception;

import com.edgar.lifeos.common.api.ErrorCode;

/**
 * 状态冲突，如重复创建、并发修改，对应 409。
 */
public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}