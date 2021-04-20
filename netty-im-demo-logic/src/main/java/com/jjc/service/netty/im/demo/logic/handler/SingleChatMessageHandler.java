package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.enums.ErrorCode;
import com.jjc.service.netty.im.demo.common.meaasge.ChatRedirectToUserMessage;
import com.jjc.service.netty.im.demo.common.meaasge.ChatSendResponse;
import com.jjc.service.netty.im.demo.common.meaasge.SingleChatMessage;
import com.jjc.service.netty.im.demo.logic.manager.NettyChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: TODO 对于实际im，客户端应该通过短连接(http)调用进行收件箱的写入，然后外部服务再通过调用长连接的push接口，进行指定用户的消息推送，而不是客户端-长连接-客户端的链路
 * 应该是 客户端A-im服务端-长连接push-客户端B，将服务职责单一化
 * @author: jjc
 * @createTime: 2021/4/21
 */
@Slf4j
@Component
public class SingleChatMessageHandler implements MessageHandler<SingleChatMessage> {

    private final NettyChannelManager nettyChannelManager;
    public SingleChatMessageHandler(NettyChannelManager nettyChannelManager) {
        this.nettyChannelManager = nettyChannelManager;
    }

    @Override
    public void execute(Channel channel, SingleChatMessage message) {
        log.info("[execute][收到连接({}) 的用户({})发给用户({})的消息]，进行消息投递", channel.id(), message.getFromUser(), message.getToUser());
        // 省略一系列处理链路，消息发送成功
        ChatSendResponse chatSendResponse = new  ChatSendResponse()
                .setCode(ErrorCode.SUCCESS.getCode())
                .setMessage(ErrorCode.SUCCESS.getDesc())
                .setMsgId(message.getMsgId());
        // 原channel连接写回消息发送成功的响应
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE, chatSendResponse));
        // 发送给指定用户
        ChatRedirectToUserMessage chatRedirectToUserMessage = new  ChatRedirectToUserMessage()
                .setFromUser(message.getFromUser())
                .setToUser(message.getToUser())
                .setMsgId(message.getMsgId())
                .setContent(message.getContent());
        nettyChannelManager.sendMessage(message.getToUser(), new Invocation(ChatRedirectToUserMessage.TYPE, chatRedirectToUserMessage));
    }

    @Override
    public String getType() {
        return SingleChatMessage.TYPE;
    }
}