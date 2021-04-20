package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.meaasge.ChatRedirectToUserMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/21
 */
@Slf4j
@Component
public class ChatRedirectToUserMessageHandler implements MessageHandler<ChatRedirectToUserMessage> {

    @Override
    public void execute(Channel channel, ChatRedirectToUserMessage message) {
        log.info("[execute][收到消息：{}]", message);
    }

    @Override
    public String getType() {
        return ChatRedirectToUserMessage.TYPE;
    }
}