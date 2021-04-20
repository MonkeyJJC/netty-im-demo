package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description: 服务端认证响应
 * @author: jjc
 * @createTime: 2021/4/19
 */
public class AuthResponse implements Message {
    public static final String TYPE = "AUTH_RESPONSE";
    /**
     * @see com.jjc.service.netty.im.demo.common.enums.ErrorCode
     */
    protected Integer code;
    protected String message;

    public Integer getCode() {
        return code;
    }

    public AuthResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AuthResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}