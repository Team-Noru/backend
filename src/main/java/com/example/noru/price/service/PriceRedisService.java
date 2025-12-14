package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "price:";


    public void savePrice(String companyId, String price) {
        stringRedisTemplate.opsForValue().set(PREFIX + companyId, price);
    }

    public String get(String companyId) {
        return stringRedisTemplate.opsForValue().get(PREFIX + companyId);
    }
}

