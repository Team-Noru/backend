package com.example.noru.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PriceFetchService {

    private final RestClient restClient;

    @Value("${app.korea-invest.base-url}")
    private String baseUrl;
    @Value("${app.korea-invest.appkey}")
    private String appKey;
    @Value("${app.korea-invest.appsecret}")
    private String appSecret;

    public String fetchPrice(String token, String code) {

        return restClient.get()
                .uri(baseUrl + "/uapi/domestic-stock/v1/quotations/inquire-price",
                        uri -> uri.queryParam("FID_COND_MRKT_DIV_CODE", "J")
                                .queryParam("FID_INPUT_ISCD", code)
                                .build())
                .header("authorization", "Bearer " + token)
                .header("appkey", appKey)
                .header("appsecret", appSecret)
                .header("tr_id", "FHKST01010100")
                .retrieve()
                .body(String.class);
    }
}
