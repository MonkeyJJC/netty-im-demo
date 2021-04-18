package com.jjc.service.netty.im.demo.api;

import com.jjc.service.netty.im.demo.service.NettyImDemoServiceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;


/**
 * The type Spring template api application context.
 * 服务端启动类
 * @author jjc
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(value = {
        NettyImDemoServiceContext.class
})
public class NettyImDemoApiApplicationContext {

    public static void main(String[] args) {
        SpringApplication.run(NettyImDemoApiApplicationContext.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer(){
        return new TomcatServletWebServerFactory(8081) ;
    }
}
