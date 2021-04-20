package com.jjc.service.netty.im.demo.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: json工具类
 * @author: jjc
 * @createTime: 2021/4/21
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static byte[] toJsonBytes(Object object) {
        if (null == object) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return (T)mapper.readValue(json, clazz);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> type){
        try {
            return (T)mapper.readValue(json, type);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T fromJson(byte[] json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T fromJson(InputStream stream, Class<T> clazz) {
        try {
            return mapper.readValue(stream, clazz);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * demo:map转为class
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    /**
     * demo：bean转为map，e.g: TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {};
     * @param object
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T convert(Object object, TypeReference<T> type) {
        return mapper.convertValue(object, type);
    }
}