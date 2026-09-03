package com.edgar.lifeos.server.exception;

import com.edgar.lifeos.common.api.ErrorCode;
import com.edgar.lifeos.common.api.Result;
import com.edgar.lifeos.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(com.edgar.lifeos.common.exception.NotFoundException.class)
    public Result<Void> handleNotFound(com.edgar.lifeos.common.exception.NotFoundException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(com.edgar.lifeos.common.exception.ForbiddenException.class)
    public Result<Void> handleForbidden(com.edgar.lifeos.common.exception.ForbiddenException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(com.edgar.lifeos.common.exception.ConflictException.class)
    public Result<Void> handleConflict(com.edgar.lifeos.common.exception.ConflictException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("参数错误");
        return Result.failure(ErrorCode.BAD_REQUEST, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未处理异常", e);
        return Result.failure(ErrorCode.INTERNAL_ERROR, "系统内部错误");
    }
}