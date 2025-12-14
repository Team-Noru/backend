package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceRedisService {

    private final StringRedisTemplate redis;
    private static final String PREFIX = "price:";

    public void saveDomestic(String stockCode, String json) {
        redis.opsForValue().set(
                PREFIX + "DOMESTIC:" + stockCode,
                json
        );
    }

    public void saveOverseas(String exchange, String stockCode, String json) {
        redis.opsForValue().set(
                PREFIX + exchange + ":" + stockCode,
                json
        );
    }

    public String get(String exchange, String stockCode) {
        if (exchange == null) {
            return redis.opsForValue()
                    .get(PREFIX + "DOMESTIC:" + stockCode);
        }
        return redis.opsForValue()
                .get(PREFIX + exchange + ":" + stockCode);
    }
}
