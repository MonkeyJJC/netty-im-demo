package com.jjc.service.netty.im.demo.api;

import com.jjc.service.netty.im.demo.api.handler.NettyServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @description: Netty服务端引导
 * @author: jjc
 * @createTime: 2021/4/13
 */
@Component
@Slf4j
public class NettyServer {

    /**
     * 读取application.yml配置
     */
    @Value("${netty.port}")
    private Integer port;
    /**
     * Netty Server服务端Channel
     */
    private Channel channel;
    /**
     * bossGroup负责服务端接收客户端的连接:
     * 1.接收客户端TCP连接，初始化Channel参数；
     * 2.将链路状态变更事件通知给ChannelPipeline；
     * 为什么主从(两个线程组)优势：创建专门用于接受客户端连接的 bossGroup 线程组，避免因为已连接的客户端的数据读写频繁，影响新的客户端的连接。
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * workerGroup用于服务端接受客户端的数据读写,负责处理IO相关的读写操作或者执行系统task、定时task等
     * 1.异步读取远端数据，发送读事件到ChannelPipeline；
     * 2.异步发送数据到远端，调用ChannelPipeline的发送消息接口；
     * 3.执行系统调用Task；
     * 4.执行定时任务Task，如空闲链路检测等；
     * 为什么多线程优势：创建专门用于接收客户端读写的 workerGroup 线程组，多个线程进行客户端的数据读写，可以支持更多客户端。
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final NettyServerHandlerInitializer nettyServerHandlerInitializer;

    public NettyServer(NettyServerHandlerInitializer nettyServerHandlerInitializer) {
        this.nettyServerHandlerInitializer = nettyServerHandlerInitializer;
    }

    /**
     * 启动Netty Server
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 创建引导，用于netty启动
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 在创建ServerBootstrap类实例前，先创建两个EventLoopGroup，它们实际上是两个独立的Reactor线程池
        // Netty默认线程模型是「主从Reactor多线程模型」，
        serverBootstrap.group(bossGroup, workerGroup)
                // 指定 Channel为服务端NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                // 设置服务端accept队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 允许较小的数据包的发送，降低延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 设置客户端连接上来的 Channel 的处理器为 NettyServerHandlerInitializer
                // 在每一个客户端与服务端建立完成连接时，服务端会创建一个Channel与之对应, NettyServerHandlerInitializer会进行执行initChannel(Channel c) 方法，进行自定义的初始化
                .childHandler(nettyServerHandlerInitializer);
        // 绑定端口，并同步等待成功，即启动Netty Server服务端
        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            // 启动成功，获取Channel引用
            channel = future.channel();
            log.info("start Netty Server port {}", port);
        }
    }

    /**
     * 关闭 Netty Server
     */
    @PreDestroy
    public void gracefulShutdown() {
        if (Objects.nonNull(channel)) {
            channel.close();
        }
        // 线程组优雅关闭，例如线程池资源等
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}