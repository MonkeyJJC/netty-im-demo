package com.jjc.service.netty.im.demo.common.exception;

/**
 * @author jjc
 */
public class ArgumentException extends IllegalArgumentException {
    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable clause) {
        super(message, clause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
