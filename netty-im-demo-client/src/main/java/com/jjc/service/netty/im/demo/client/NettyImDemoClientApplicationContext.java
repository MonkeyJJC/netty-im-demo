package com.jjc.service.netty.im.demo.client;

import com.jjc.service.netty.im.demo.service.NettyImDemoServiceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @description: 客户端启动类
 * @author: jjc
 * @createTime: 2021/4/15
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(value = {
        NettyImDemoServiceContext.class
})
public class NettyImDemoClientApplicationContext {

    public static void main(String[] args) {
        SpringApplication.run(NettyImDemoClientApplicationContext.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer(){
        return new TomcatServletWebServerFactory(8083) ;
    }
}