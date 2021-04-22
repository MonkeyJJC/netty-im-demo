package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.enums.ErrorCode;
import com.jjc.service.netty.im.demo.common.meaasge.ChatRedirectToUserMessage;
import com.jjc.service.netty.im.demo.common.meaasge.ChatSendResponse;
import com.jjc.service.netty.im.demo.common.meaasge.GroupChatMessage;
import com.jjc.service.netty.im.demo.logic.manager.NettyChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 群聊消息处理类，这里demo起见，发给所有注册的channel，实际im应考虑读/写扩散等方式
 * @author: jjc
 * @createTime: 2021/4/22
 */
@Slf4j
@Component
public class GroupChatMessageHandler implements MessageHandler<GroupChatMessage> {

    private final NettyChannelManager nettyChannelManager;
    public GroupChatMessageHandler(NettyChannelManager nettyChannelManager) {
        this.nettyChannelManager = nettyChannelManager;
    }

    @Override
    public void execute(Channel channel, GroupChatMessage message) {
        // 消息发送回执
        ChatSendResponse sendResponse = new ChatSendResponse().setMsgId(message.getMsgId()).setCode(ErrorCode.SUCCESS.getCode()).setMessage(ErrorCode.SUCCESS.getDesc());
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE, sendResponse));
        // ...
        ChatRedirectToUserMessage chatRedirectToUserMessage = new  ChatRedirectToUserMessage()
                .setFromUser(message.getFromUser())
                .setMsgId(message.getMsgId())
                .setContent(message.getContent());
        nettyChannelManager.broadcastMessage(new Invocation(ChatRedirectToUserMessage.TYPE, chatRedirectToUserMessage));
    }

    @Override
    public String getType() {
        return GroupChatMessage.TYPE;
    }
}