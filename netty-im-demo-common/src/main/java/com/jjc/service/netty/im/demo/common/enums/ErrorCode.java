package com.jjc.service.netty.im.demo.common.enums;

import com.jjc.service.netty.im.demo.common.enums.BaseEnum;

/**
 * @author jjc
 */
public enum ErrorCode implements BaseEnum {
    SUCCESS(10000, null),
    BAD_PARAMS(30000, "参数错误"),
    SYSTEM_EXCEPTION(40000, "系统错误"),
    SERVICE_HAD_NOT_READY(4000003, "SYSTEM_EXCEPTION"),
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
