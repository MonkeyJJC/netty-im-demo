package com.jjc.service.netty.im.demo.common.dto;

import com.alibaba.fastjson.JSON;
import com.jjc.service.netty.im.demo.common.meaasge.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 通信协议的消息体
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invocation {
    /**
     * 类型
     */
    private String type;
    /**
     * 消息，JSON 格式
     */
    private String message;

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}