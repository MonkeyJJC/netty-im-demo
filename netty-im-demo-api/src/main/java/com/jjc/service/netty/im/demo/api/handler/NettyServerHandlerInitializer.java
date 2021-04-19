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
                // 服务端空闲检测，在超过指定时间未从对端读取到数据，会抛出ReadTimeoutException异常，并关闭channel
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                // 自定义协议的编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // TODO 如果看做一个标准的im系统，这里将会拆为多个微服务，例如业务处理，消息处理，收件箱，存储，离线推送等
                // 思路1：本长连接服务封装个push接口对外提供，跟im消息处理等服务解耦
                .addLast(messageDispatcher)
                // 服务端业务处理
                .addLast(nettyServerHandler)
                ;
    }
}