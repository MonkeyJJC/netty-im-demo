package com.jjc.service.netty.im.demo.client.handler;

import com.jjc.service.netty.im.demo.client.NettyClient;
import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.meaasge.HeartbeatRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/15
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClient nettyClient;

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }

    /**
     * 当你的链接断开的时候，这个方法会被netty框架异步调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Lost the connection with the server.");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> {
            log.info("client reconnecting ...");
            nettyClient.reconnect();
        }, 20, TimeUnit.SECONDS);
        // 继续触发事件,即fireChannel方法将消息在pipeline中传递下去，传递至下一个处理器
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client channel exception {}", ctx.channel().id(), cause);
        // 发生异常断开连接
        ctx.channel().close();
    }

    /**
     * userEventTriggered是Netty处理心跳超时事件，在IdleStateHandler设置超时时间，如果达到了，就会直接调用该方法。
     * 如果没有超时则不调用。我们重写该方法的话，就可以自行进行相关的业务逻辑处理了
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 空闲时，向服务端发起一次心跳，即心跳机制
        if (evt instanceof IdleStateEvent) {
            log.info("[userEventTriggered][发起一次心跳]");
            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            ctx.writeAndFlush(new Invocation(HeartbeatRequest.TYPE, heartbeatRequest))
                    // A ChannelFutureListener that closes the Channel when the operation ended up with a failure or cancellation rather than a success.
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}