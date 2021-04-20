package com.jjc.service.netty.im.demo.client;

import com.jjc.service.netty.im.demo.client.handler.NettyClientHandlerInitializer;
import com.jjc.service.netty.im.demo.common.dto.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 客户端引导类
 * @author: jjc
 * @createTime: 2021/4/15
 */
@Slf4j
@Component
public class NettyClient {

    /**
     * 重连频率，单位：秒
     */
    private static final Integer RECONNECT_SECONDS = 20;
    @Value("${netty.server.host}")
    private String serverHost;
    @Value("${netty.server.port}")
    private Integer serverPort;
    /**
     * 线程组，用于客户端对服务端的链接、数据读写
     */
    private EventLoopGroup eventGroup = new NioEventLoopGroup();
    /**
     * Netty Client Channel
     */
    private Channel channel;

    private final NettyClientHandlerInitializer nettyClientHandlerInitializer;

    public NettyClient(NettyClientHandlerInitializer nettyClientHandlerInitializer) {
        this.nettyClientHandlerInitializer = nettyClientHandlerInitializer;
    }

    /**
     * Netty Client启动
     */
    @PostConstruct
    public void start() {
        // 创建引导
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventGroup)
                // 指定 Channel 为客户端 NioSocketChannel
                .channel(NioSocketChannel.class)
                // 指定链接服务器的地址
                .remoteAddress(serverHost, serverPort)
                // TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 允许较小的数据包的发送，降低延迟
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(nettyClientHandlerInitializer);
        // 连接服务端，并异步等待成功，即启动客户端
        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            // 连接失败
            if (!future.isSuccess()) {
                log.error("[start][Netty Client 连接服务器({}:{}) 失败]", serverHost, serverPort);
                reconnect();
                return;
            }
            // 连接成功
            channel = future.channel();
            log.info("[start][Netty Client 连接服务器({}:{}) 成功]", serverHost, serverPort);
        });
    }

    public void reconnect() {
        eventGroup.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("[reconnect][开始重连]");
                try {
                    start();
                } catch (Exception e) {
                    log.error("[reconnect][重连失败]", e);
                }
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);
        log.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }

    @PreDestroy
    public void gracefulShutdown() {
        if (Objects.nonNull(channel)) {
            // 关闭Netty Client
            channel.close();
        }
        eventGroup.shutdownGracefully();
    }

    /**
     * 发送消息
     *
     * @param invocation 消息体
     */
    public void send(Invocation invocation) {
        if (Objects.isNull(channel)) {
            log.error("client send failed, channel do not exist");
            return;
        }
        if (!channel.isActive()) {
            log.error("channel is not active {}", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }
}