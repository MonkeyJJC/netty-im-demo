package com.jjc.service.netty.im.demo.common.enums;

import com.jjc.service.netty.im.demo.common.enums.BaseEnum;

/**
 * @author jjc
 */
public enum ErrorCode implements BaseEnum {
    SUCCESS(10000, "成功"),
    AUTH_FAILED(10001, "认证失败"),
    BAD_PARAMS(30000, "参数错误"),
    SYSTEM_EXCEPTION(40000, "系统错误"),
    ;

    private final int code;
    private final String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
