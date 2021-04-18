package com.jjc.service.netty.im.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * The type Spring template common application context.
 * @author jjc
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(value = "com.jjc.service.netty.im.demo.common")
public class NettyImDemoCommonApplicationContext {

    /**
     * Logger Template
     */
    private static Logger LOGGER = LoggerFactory.getLogger(NettyImDemoCommonApplicationContext.class);
}
