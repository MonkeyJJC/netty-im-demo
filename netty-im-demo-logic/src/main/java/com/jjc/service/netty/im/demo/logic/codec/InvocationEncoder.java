package com.jjc.service.netty.im.demo.logic.codec;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/14
 */
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    @Override
    public void encode(ChannelHandlerContext ctx, Invocation msg, ByteBuf out) {

    }
}