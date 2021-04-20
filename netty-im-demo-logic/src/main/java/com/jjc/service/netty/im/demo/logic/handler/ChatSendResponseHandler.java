package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.meaasge.ChatSendResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/22
 */
@Slf4j
@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse> {

    @Override
    public void execute(Channel channel, ChatSendResponse message) {
        log.info("[execute][连接({}) 消息({})发送成功]", channel.id(), message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }
}