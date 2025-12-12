package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceRedisService {
    private final RedisTemplate<String, String> redis;

    private static final String PREFIX = "price:";

    public void save(String code, String json) {
        redis.opsForValue().set(PREFIX + code, json);
    }

    public String get(String code) {
        return redis.opsForValue().get(PREFIX + code);
    }
}
