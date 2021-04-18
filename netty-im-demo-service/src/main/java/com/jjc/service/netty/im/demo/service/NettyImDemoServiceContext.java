package com.jjc.service.netty.im.demo.service;

import com.jjc.service.netty.im.demo.logic.NettyImDemoLogicContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jjc
 * Description: service模块实现contract中定义的接口
 */

@Configuration
@ComponentScan(value = "com.jjc.service.netty.im.demo.service")
@Import(value = {
        NettyImDemoLogicContext.class
})
public class NettyImDemoServiceContext {
}
