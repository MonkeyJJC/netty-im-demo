package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.meaasge.AuthResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 客户端处理服务端认证回执
 * @author: jjc
 * @createTime: 2021/4/19
 */
@Slf4j
@Component
public class AuthResponseHandler implements MessageHandler<AuthResponse> {

    @Override
    public void execute(Channel channel, AuthResponse message) {
        log.info("[execute][收到连接({}) 的认证响应结果({})]", channel.id(), message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }
}