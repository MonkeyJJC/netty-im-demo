package com.jjc.service.netty.im.demo.logic.codec;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 编码器
 * 解决粘包与拆包：将消息分为头部和消息体，在头部中保存有当前整个消息的长度，只有在读取到足够长度的消息之后才算是读到了一个完整的消息。
 * 协议约定：头部插入一个int来表示当条消息内容长度，用于解码时解析
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Slf4j
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    /**
     * 将ByteBuf out 写到TCP Socket中
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Invocation msg, ByteBuf out) {
        // 将 Invocation 转换成 byte[] 数组
        // TCP传输需要自己基于二进制构建，构建客户端和服务端的通信协议。无法将一个Java对象直接丢到TCP Socket当中，而是需要将其转换成byte字节数组，才能写入到TCP Socket中去。即需要将消息对象通过序列化，转换成byte字节数组。
        // TODO 序列化方式优化。简单起见，采用JSON方式进行序列化。后续可改为Protobuf进行序列化，性能更高，且序列化出来的二进制数据较小，ProtobufEncoder
        byte[] content = JsonUtils.toJsonBytes(msg);
        // 写入长度
        out.writeInt(content.length);
        // 写入内容
        out.writeBytes(content);
        log.info("[encode][连接({}) 编码了一条消息({})]", ctx.channel().id(), msg.toString());
    }
}