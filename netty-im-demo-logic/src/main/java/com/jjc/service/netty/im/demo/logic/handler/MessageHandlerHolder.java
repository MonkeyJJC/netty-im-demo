package com.jjc.service.netty.im.demo.logic.handler;

import com.google.common.collect.ImmutableMap;
import com.jjc.service.netty.im.demo.common.meaasge.Message;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @description: MessageHandler容器
 * @author: jjc
 * @createTime: 2021/4/19
 */
@Component
public class MessageHandlerHolder implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Map<String, MessageHandler> holderMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        if (Objects.isNull(holderMap)) {
            this.holderMap = applicationContext.getBeansOfType(MessageHandler.class).values().stream()
                    .map(messageHandler -> newEntry(messageHandler.getType(), messageHandler))
                    .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    private static <K, V> Map.Entry<K, V> newEntry(K key, V value) {
        return Collections.singletonMap(key, value).entrySet().iterator().next();
    }

    public MessageHandler getMessageHandler(String type) {
        MessageHandler handler = holderMap.get(type);
        if (Objects.isNull(handler)) {
            throw new IllegalArgumentException("类型" + type + "找不到匹配的MessageHandler处理器");
        }
        return handler;
    }

    public static Class<? extends Message> getMessageClass(MessageHandler handler) {
        // 获得Bean对应的Class类。因为有可能被AOP代理过。
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();
        // 以父类的接口为准
        while (0 == interfaces.length && Objects.nonNull(superclass)) {
            interfaces = superclass.getGenericInterfaces();
            superclass = targetClass.getSuperclass();
        }
        // 遍历 interfaces 数组
        for (Type type : interfaces) {
            // type是泛型参数
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                // MessageHandler接口
                if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    // 取首个元素
                    if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                        return (Class<Message>) actualTypeArguments[0];
                    } else {
                        throw new IllegalStateException("类型"+ handler + "获得不到消息类型");
                    }
                }
            }
        }
        throw new IllegalStateException("类型"+ handler + "获得不到消息类型");
    }
}