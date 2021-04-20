package com.jjc.service.netty.im.demo.logic.manager;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.utils.ServiceUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: 客户端Channel管理
 * 1.客户端Channel管理
 * 2.向客户端Channel发送消息
 * TODO 简单起见，channel等连接信息内存维护
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Slf4j
@Component
public class NettyChannelManager {

    /**
     * {@link Channel#attr(AttributeKey)} 属性中，表示Channel对应的用户
     * AttributeMap这是是绑定在Channel或者ChannelHandlerContext上的一个附件
     * 每一个ChannelHandlerContext上如果有AttributeMap都是绑定上下文的，也就说如果A的ChannelHandlerContext中的AttributeMap，B的ChannelHandlerContext是无法读取到的
     * 但是Channel上的AttributeMap就是大家共享的，每一个ChannelHandler都能获取到
     */
    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");
    private final ConcurrentMap<ChannelId, Channel> channels = new ConcurrentHashMap<>();
    /**
     * 用户channel映射
     */
    private final ConcurrentMap<String, Channel> userChannels = new ConcurrentHashMap<>();

    public void add(Channel channel) {
        Channel newChannel = channels.putIfAbsent(channel.id(), channel);
        if (Objects.isNull(newChannel)) {
            log.info("[add][一个连接({})加入]", channel.id());
        }
    }

    public void userRegister(Channel channel, String user) {
        Channel existChannel = channels.get(channel.id());
        if (Objects.isNull(existChannel)) {
            log.error("[userRegister][连接({}) 不存在]", channel.id());
            return;
        }
        channel.attr(CHANNEL_ATTR_KEY_USER).set(user);
        userChannels.putIfAbsent(user, channel);
        log.error("[userRegister][连接({}) 用户({})注册]", channel.id(), user);
    }

    public void remove(Channel channel) {
        channels.remove(channel.id());
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    public void sendMessage(String user, Invocation message) {
        Channel userChannel = userChannels.get(user);
        if (Objects.isNull(userChannel)) {
            log.error("[sendMessage][连接不存在]");
            return;
        }
        if (!userChannel.isActive()) {
            log.error("[sendMessage][连接({})未激活]", userChannel.id());
        }
        userChannel.writeAndFlush(message);
    }

    public void broadcastMessage(Invocation message) {
        userChannels.values().stream()
                .filter(Objects::nonNull)
                .filter(Channel::isActive)
                .forEach(channel ->
                        ServiceUtils.runWithFallback(() -> channel.writeAndFlush(message), "broadcastMessage failed " + channel.id())
                );
    }
}