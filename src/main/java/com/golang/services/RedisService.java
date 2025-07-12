package com.golang.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisRestTemplate;

    public void save(String key, String value){
        redisRestTemplate.opsForValue().set(key, value, Duration.ofMinutes(10));
    }

    public Object get(String key){
        return redisRestTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisRestTemplate.delete(key);
    }
}
