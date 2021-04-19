package com.jjc.service.netty.im.demo.client.handler;

import com.jjc.service.netty.im.demo.client.NettyClient;
import com.jjc.service.netty.im.demo.logic.codec.InvocationDecoder;
import com.jjc.service.netty.im.demo.logic.codec.InvocationEncoder;
import com.jjc.service.netty.im.demo.logic.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/15
 */
@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 60;

    private MessageDispatcher messageDispatcher;
    @Autowired
    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    private final NettyClientHandler nettyClientHandler;
    public NettyClientHandlerInitializer(NettyClient nettyClient) {
        this.nettyClientHandler = new NettyClientHandler(nettyClient);
    }

    @Override
    public void initChannel(Channel ch) {
        ch.pipeline()
                // 心跳机制，在Netty中，提供IdleStateHandler类，可以实现对三种心跳的检测，分别是readerIdleTime(为读超时时间,即测试端一定时间内未接受到被测试端消息)、writerIdleTime(为写超时时间即测试端一定时间内向被测试端发送消息)和allIdleTime(所有类型的超时时间)。
                // 在Channel的读或者写空闲时间太长时，将会触发一个IdleStateEvent事件，在NettyClientHandler处理器中，在接收到IdleStateEvent事件时(userEventTriggered方法)，客户端向客户端发送一次心跳消息。
                .addLast(new IdleStateHandler(READ_TIMEOUT_SECONDS, 0, 0))
                // 空闲检测，客户端发现180秒未从服务端读取到消息，主动断开连接
                .addLast(new ReadTimeoutHandler(3 * READ_TIMEOUT_SECONDS))
                .addLast(new InvocationEncoder())
                .addLast(new InvocationDecoder())
                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(nettyClientHandler);
    }
}