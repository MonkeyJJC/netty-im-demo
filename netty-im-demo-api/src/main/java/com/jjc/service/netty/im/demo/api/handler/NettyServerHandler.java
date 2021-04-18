package com.jjc.service.netty.im.demo.api.handler;

import com.jjc.service.netty.im.demo.logic.manager.NettyChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 服务端Channel实现类，提供对客户端Channel建立连接、断开连接、异常时的处理
 * @author: jjc
 * @createTime: 2021/4/13
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final NettyChannelManager nettyChannelManager;

    public NettyServerHandler(NettyChannelManager nettyChannelManager) {
        this.nettyChannelManager = nettyChannelManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 客户端和服务端建立连接完成时
        nettyChannelManager.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        // 客户端和服务端断开连接时
        nettyChannelManager.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // channel的事件发生异常时，调用 Channel 的 #close() 方法，断开和客户端的连接
        log.error("exceptionCaught channel {}", ctx.channel().id(), cause);
        // 连接异常，断开连接
        ctx.channel().close();
    }

}