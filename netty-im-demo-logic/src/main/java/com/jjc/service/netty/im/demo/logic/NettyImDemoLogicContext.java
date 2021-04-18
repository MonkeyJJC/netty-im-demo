package com.jjc.service.netty.im.demo.logic;

import com.jjc.service.netty.im.demo.common.NettyImDemoCommonApplicationContext;
import com.jjc.service.netty.im.demo.integration.NettyImDemoIntegrationContext;
import com.jjc.service.netty.im.demo.repository.NettyImDemoRepositoryContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/15
 */
@Configuration
@ComponentScan
@Import(value = {
        NettyImDemoIntegrationContext.class,
        NettyImDemoCommonApplicationContext.class,
        NettyImDemoRepositoryContext.class
})
public class NettyImDemoLogicContext {
}