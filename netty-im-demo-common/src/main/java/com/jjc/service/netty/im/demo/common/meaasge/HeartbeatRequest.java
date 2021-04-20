package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description: 心跳请求
 * @author: jjc
 * @createTime: 2021/4/16
 */
public class HeartbeatRequest implements Message {
    /**
     * 类型 - 客户端心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }
}