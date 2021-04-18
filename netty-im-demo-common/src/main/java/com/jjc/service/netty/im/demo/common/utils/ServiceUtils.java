package com.jjc.service.netty.im.demo.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: jjc
 * @createTime: 2021/4/14
 */
@Slf4j
public class ServiceUtils {
    public static void runWithFallback(Runnable runnable, String errorMessage) {
        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("execute failed {}", errorMessage, ex);
        }
    }
}