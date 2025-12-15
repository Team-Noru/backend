package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PriceRedisService {

    private final StringRedisTemplate redis;
    private static final String PREFIX = "price:";

    // TTL 정책
    private static final long DOMESTIC_TTL_SEC = 60 * 60 * 24;
    private static final long OVERSEAS_TTL_SEC = 60 * 60 * 24;

    /* =========================
     * 🇰🇷 국내
     * ========================= */
    public void saveDomestic(String stockCode, String json) {
        redis.opsForValue().set(
                PREFIX + "DOMESTIC:" + stockCode,
                json,
                DOMESTIC_TTL_SEC,
                TimeUnit.SECONDS
        );
    }

    /* =========================
     * 🌍 해외
     * ========================= */
    public void saveOverseas(String exchange, String stockCode, String json) {
        redis.opsForValue().set(
                PREFIX + exchange + ":" + stockCode,
                json,
                OVERSEAS_TTL_SEC,
                TimeUnit.SECONDS
        );
    }

    /* =========================
     * 공통 조회
     * ========================= */
    public String get(String exchange, String stockCode) {
        if (exchange == null) {
            return redis.opsForValue()
                    .get(PREFIX + "DOMESTIC:" + stockCode);
        }
        return redis.opsForValue()
                .get(PREFIX + exchange + ":" + stockCode);
    }
}
