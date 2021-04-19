package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.meaasge.HeartbeatRequest;
import com.jjc.service.netty.im.demo.common.meaasge.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 服务端心跳消息处理器，在收到客户端的心跳请求时，回复客户端一条确认消息
 * @author: jjc
 * @createTime: 2021/4/19
 */
@Slf4j
@Component
public class HeartbeatServerHandler implements MessageHandler<HeartbeatRequest> {

    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        HeartbeatResponse response = new HeartbeatResponse();
        // 服务端收到心跳，给客户端发送响应
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}