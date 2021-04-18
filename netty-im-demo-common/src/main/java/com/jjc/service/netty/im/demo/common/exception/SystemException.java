package com.jjc.service.netty.im.demo.common.exception;

import com.jjc.service.netty.im.demo.common.enums.ErrorCode;

/**
 * @author jjc
 */
public class SystemException extends RuntimeException{
    private int code;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
    }

    public SystemException(String message, int code) {
        super(message);
        this.code = code;
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDesc(), cause);
        this.code = errorCode.getCode();
    }

    public SystemException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }
}
