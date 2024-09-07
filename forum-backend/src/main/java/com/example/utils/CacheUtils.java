package com.example.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtils {
    @Resource
    StringRedisTemplate template;

    public <T> T takeFromCache(String key, Class<T> dataType){
        String s = template.opsForValue().get(key);
        return s == null ? null : JSONArray.parseArray(s).to(dataType);
    }

    public <T> List<T> takeListFromCache(String key, Class<T> itemType){
        String s = template.opsForValue().get(key);
        return s == null ? null : JSONArray.parseArray(s).toList(itemType);
    }



    public <T> void saveToCache(String key, T data, long expire){
        template.opsForValue().set(key, JSON.toJSONString(data), expire, TimeUnit.SECONDS);
    }

    public <T> void saveListToCache(String key, List<T> list, long expire){
        template.opsForValue().set(key, JSON.toJSONString(list), expire, TimeUnit.SECONDS);
    }

    public void deleteCachePattern(String key){
        Set<String> keys = Optional.ofNullable(template.keys(key)).orElse(Collections.emptySet());
        template.delete(key);
    }

    public void deleteCache(String key){
        template.delete(key);
    }
}
