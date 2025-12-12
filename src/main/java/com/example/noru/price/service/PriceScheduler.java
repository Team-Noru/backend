package com.example.noru.price.service;

import com.example.noru.price.domain.StockCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceScheduler {

    private final TokenService tokenService;
    private final PriceFetchService fetchService;
    private final PriceRedisService priceRedisService;

    @Scheduled(fixedRate = 30000)
    public void updateAllPrices() {

        String token = tokenService.getAccessToken();

        for (String code : StockCode.CODES) {

            try {
                String json = fetchService.fetchPrice(token, code);
                priceRedisService.save(code, json);
            } catch (Exception e) {
                System.out.println("❌ Error fetching " + code + ": " + e.getMessage());
            }

            // Rate-limit 보호용 (필요시)
            try { Thread.sleep(150); } catch (InterruptedException ignore) {}
        }
    }
}
