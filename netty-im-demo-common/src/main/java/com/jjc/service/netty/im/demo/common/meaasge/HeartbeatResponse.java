package com.jjc.service.netty.im.demo.common.meaasge;

import lombok.Data;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/19
 */
@Data
public class HeartbeatResponse implements Message {
    /**
     * 类型 - 服务端心跳相应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";
}