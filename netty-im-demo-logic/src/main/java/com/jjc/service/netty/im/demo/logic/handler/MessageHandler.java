package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.meaasge.Message;
import io.netty.channel.Channel;

/**
 * @description: message处理
 * @author: jjc
 * @createTime: 2021/4/14
 */
public interface MessageHandler<T extends Message> {
    /**
     * 执行处理消息
     * @param channel 通道
     * @param message 消息
     */
    void execute(Channel channel, T message);

    /**
     * 消息类型
     * @return
     */
    String getType();
}