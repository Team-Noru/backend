package com.example.noru.price.service;

import com.example.noru.company.rds.entity.Company;
import com.example.noru.price.config.PriceParsingConfig;
import com.example.noru.price.dto.PriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class PriceGraphService {

    private final TokenService tokenService;
    private final PriceFetchService domesticFetchService;
    private final PriceRedisService priceRedisService;

    public PriceDto getPrice(String ticker) {

        String json = priceRedisService.get(null, ticker);
        if (json != null) {
            return PriceParsingConfig.parsePrice(ticker, json);
        }

        try {
            String token = tokenService.getAccessToken();
            json = domesticFetchService.fetchPrice(token, ticker);

            priceRedisService.saveDomestic(ticker, json);

            return PriceParsingConfig.parsePrice(ticker, json);

        } catch (Exception e) {
            return new PriceDto(ticker, -1, 0, 0.0);
        }
    }
}
