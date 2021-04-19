package com.jjc.service.netty.im.demo.logic.dispatcher;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.meaasge.Message;
import com.jjc.service.netty.im.demo.logic.handler.MessageHandler;
import com.jjc.service.netty.im.demo.logic.handler.MessageHandlerHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 消息处理层，作为demo，初步简单定制化实现下
 * TODO 这里其实可以单独拆为一个消息业务层的微服务，用于消息业务信息的处理，例如各种filter，context上下文处理，存储属性、会话属性及推送属性等设置
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Service
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    private static final ThreadFactory FUTURE_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("future-pool-%s").build();
    private static final int MAX_QUEUE_SIZE = 100;
    /**
     * 业务线程池，用于执行业务逻辑（dubbo等RPC框架也是这种方式）
     * 优势：参考Netty的Reactor线程模型，EventGroup可以理解为一个线程池，并且大小是CPU_NUM*2，每个Channel仅仅会被分配到其中的一个线程上，进行数据的读写。
     * 并且，多个Channel 会共享一个线程，即使用同一个线程进行数据的读写。假如IO处理，例如说进行数据库的读取。这样，就会导致一个Channel在执行MessageHandler的过程中，阻塞了共享当前线程的其它Channel的数据读取。
     * 那么把EventGroup的线程池设置大些能解决么？
     * 对于长连接的Netty服务端，往往会有1000~100000的Netty客户端连接上来，这样无论设置多大的线程池，都会出现阻塞数据读取的情况。
     */
    private static final ExecutorService EXECUTOR =
            new ThreadPoolExecutor(
                    100,
                    100,
                    0,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(MAX_QUEUE_SIZE),
                    FUTURE_THREAD_FACTORY);

    private final MessageHandlerHolder messageHandlerHolder;

    public MessageDispatcher(MessageHandlerHolder messageHandlerHolder) {
        this.messageHandlerHolder = messageHandlerHolder;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation msg) {
        MessageHandler messageHandler = messageHandlerHolder.getMessageHandler(msg.getType());
        Class<? extends Message> msgClass = MessageHandlerHolder.getMessageClass(messageHandler);
        Message message = JSON.parseObject(msg.getMessage(), msgClass);
        // 不要在Netty的I/O线程上执行任何非CPU限定的代码，会影响服务器的吞吐量，一般切换到另一个不同的线程
        EXECUTOR.submit(() -> {
            // noinspection unchecked
            messageHandler.execute(ctx.channel(), message);
        });
    }
}