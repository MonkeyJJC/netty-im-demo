package com.jjc.service.netty.im.demo.common.request;

import com.jjc.service.netty.im.demo.common.meaasge.Message;
import lombok.Data;

/**
 * @description: 心跳请求
 * @author: jjc
 * @createTime: 2021/4/16
 */
@Data
public class HeartbeatRequest implements Message {
    /**
     * 类型 - 心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";
}