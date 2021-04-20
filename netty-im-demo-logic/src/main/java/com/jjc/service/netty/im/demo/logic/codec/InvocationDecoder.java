package com.jjc.service.netty.im.demo.logic.codec;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description: 解码器
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Slf4j
public class InvocationDecoder extends ByteToMessageDecoder {

    private static final Integer BYTE_NUM_OF_INT = 4;

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        if (in.readableBytes() <= BYTE_NUM_OF_INT) {
            return;
        }
        int length = in.readInt();
        if (length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }
        // 数据不足
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] content = new byte[length];
        in.readBytes(content);
        Invocation msg = JsonUtils.fromJson(content, Invocation.class);
        // 添加到List<Object> out中，交给后续的ChannelHandler进行处理
        out.add(msg);
        log.info("[decode][连接({}) 解析到一条消息({})]", ctx.channel().id(), msg.toString());
    }
}