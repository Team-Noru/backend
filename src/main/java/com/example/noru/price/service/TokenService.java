package com.example.noru.price.service;

import com.example.noru.price.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RestClient restClient;
    private final RedisTemplate<String, String> redis;

    @Value("${app.korea-invest.base-url}")
    private String baseUrl;
    @Value("${app.korea-invest.appkey}")
    private String appKey;
    @Value("${app.korea-invest.appsecret}")
    private String appSecret;

    private static final String TOKEN_KEY = "koreaInvest:accessToken";

    public String getAccessToken() {

        String cachedToken = redis.opsForValue().get(TOKEN_KEY);

        if (cachedToken != null) {
            return cachedToken;
        }

        TokenResponse response = restClient.post()
                .uri(baseUrl + "/oauth2/tokenP")
                .body(Map.of(
                        "grant_type", "client_credentials",
                        "appkey", appKey,
                        "appsecret", appSecret
                ))
                .retrieve()
                .body(TokenResponse.class);

        String newToken = response.accessToken();

        redis.opsForValue().set(TOKEN_KEY, newToken, response.expiresIn(), TimeUnit.SECONDS);

        return newToken;
    }
}
