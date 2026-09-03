package com.edgar.lifeos.common.exception;

import com.edgar.lifeos.common.api.ErrorCode;

/**
 * 资源不存在。对私有资源访问不存在时也复用此类，避免泄露资源是否存在。
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}