package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverseasPriceFetchService {

    private final RestClient restClient;

    @Value("${app.korea-invest.base-url}")
    private String baseUrl;
    @Value("${app.korea-invest.appkey}")
    private String appKey;
    @Value("${app.korea-invest.appsecret}")
    private String appSecret;

    public String fetch(String token, String exchange, String stockCode) {

        log.info("🌍 Overseas price fetch [{}:{}]", exchange, stockCode);

        return restClient.get()
                .uri(baseUrl + "/uapi/overseas-price/v1/quotations/price-detail",
                        uri -> uri
                                .queryParam("AUTH")
                                .queryParam("EXCD", exchange)
                                .queryParam("SYMB", stockCode)
                                .build())
                .header("authorization", "Bearer " + token)
                .header("appkey", appKey)
                .header("appsecret", appSecret)
                .header("tr_id", "HHDFS76200200")
                .retrieve()
                .body(String.class);
    }
}
