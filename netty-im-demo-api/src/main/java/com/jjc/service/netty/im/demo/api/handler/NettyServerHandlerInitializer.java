package com.jjc.service.netty.im.demo.api.handler;

import com.jjc.service.netty.im.demo.logic.codec.InvocationDecoder;
import com.jjc.service.netty.im.demo.logic.codec.InvocationEncoder;
import com.jjc.service.netty.im.demo.logic.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description: 用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始化操作。
 * ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里(继承于ChannelInboundHandler接口, 实际也是一个ChannelHandler)，
 * 但是在初始化完成之后，ChannelInitializer会将自己从pipeline中移除，不会影响后续的操作。
 * 使用场景：
 * a. 在ServerBootstrap初始化时，为监听端口accept事件的Channel添加ServerBootstrapAcceptor
 * b. 在有新链接进入时，为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
 * @author: jjc
 * @createTime: 2021/4/13
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    private final MessageDispatcher messageDispatcher;
    private final NettyServerHandler nettyServerHandler;

    public NettyServerHandlerInitializer(MessageDispatcher messageDispatcher, NettyServerHandler nettyServerHandler) {
        this.messageDispatcher = messageDispatcher;
        this.nettyServerHandler = nettyServerHandler;
    }

    /**
     * @param ch 此时创建的客户端 Channel
     */
    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline
                // 空闲检测
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                // 自定义协议的编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发
                .addLast(messageDispatcher)
                // 服务端业务处理
                .addLast(nettyServerHandler)
                ;
    }
}