package com.jjc.service.netty.im.demo.common.exception;

import com.jjc.service.netty.im.demo.common.enums.ErrorCode;

/**
 * @author jjc
 */
public class BusinessException extends IllegalStateException {
    private final int code;

    public BusinessException(ErrorCode errorCodeEnum) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getDesc());
    }

    public BusinessException(int errorCode, String message) {
        super(message);
        this.code = errorCode;
    }

    public BusinessException(ErrorCode errorCodeEnum, Throwable clause) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getDesc(), clause);
    }

    public BusinessException(int errorCode, String message, Throwable clause) {
        super(message, clause);
        this.code = errorCode;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getLocalizedMessage() {
        return code + ":" + super.getLocalizedMessage();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
