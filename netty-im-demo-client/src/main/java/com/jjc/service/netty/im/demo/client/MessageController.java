package com.jjc.service.netty.im.demo.client;

import com.jjc.service.netty.im.demo.common.dto.Invocation;
import com.jjc.service.netty.im.demo.common.enums.ErrorCode;
import com.jjc.service.netty.im.demo.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: mock
 * @author: jjc
 * @createTime: 2021/4/19
 */
@RestController
@RequestMapping("/api/message/")
@Slf4j
public class MessageController {

    private final NettyClient nettyClient;

    public MessageController(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @PostMapping("/send")
    public BaseResponse sendMessage(String type, String message) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(message)) {
            return BaseResponse.newFailResponse().errorCode(ErrorCode.BAD_PARAMS.getCode()).errorMsg(ErrorCode.BAD_PARAMS.getDesc()).build();
        }
        nettyClient.send(new Invocation(type, message));
        return BaseResponse.newSuccResponse().result("success").build();
    }
}