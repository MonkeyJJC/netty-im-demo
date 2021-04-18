package com.jjc.service.netty.im.demo.logic.dispatcher;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Service
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) {


        return;
    }
}