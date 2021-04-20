package com.jjc.service.netty.im.demo.common.meaasge;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/19
 */
public class HeartbeatResponse implements Message {
    /**
     * 类型 - 服务端心跳相应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }
}