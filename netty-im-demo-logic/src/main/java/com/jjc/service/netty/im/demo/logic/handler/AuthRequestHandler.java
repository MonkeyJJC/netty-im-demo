package com.jjc.service.netty.im.demo.logic.handler;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.enums.ErrorCode;
import com.jjc.service.netty.im.demo.common.meaasge.AuthRequest;
import com.jjc.service.netty.im.demo.common.meaasge.AuthResponse;
import com.jjc.service.netty.im.demo.logic.manager.NettyChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @description: 服务端处理客户端的认证请求
 * @author: jjc
 * @createTime: 2021/4/19
 */
@Slf4j
@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    private final NettyChannelManager nettyChannelManager;

    public AuthRequestHandler(NettyChannelManager nettyChannelManager) {
        this.nettyChannelManager = nettyChannelManager;
    }

    @Override
    public void execute(Channel channel, AuthRequest message) {
        log.info("[execute][收到连接({}) 的认证请求]", channel.id());
        if (StringUtils.isEmpty(message.getAccessToken())) {
            AuthResponse authResponse = new  AuthResponse()
                    .setCode(ErrorCode.AUTH_FAILED.getCode())
                    .setMessage(ErrorCode.AUTH_FAILED.getDesc());
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }
        // TODO accessToken的check及上下文信息填充等

        // 此处简单起见，直接用token作为用户长连接标识
        nettyChannelManager.userRegister(channel, message.getAccessToken());
        AuthResponse authResponse = new  AuthResponse()
                .setCode(ErrorCode.SUCCESS.getCode())
                .setMessage(ErrorCode.SUCCESS.getDesc());
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}