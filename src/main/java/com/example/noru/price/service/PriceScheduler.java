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

    private boolean isRunning = false;

    @Scheduled(cron = "0 * * * * *")
    public void updateAllPrices() {

        if (isRunning) {
            System.out.println("⏳ PriceScheduler is already running. Skipped.");
            return;
        }

        isRunning = true;

        try {
            String token = tokenService.getAccessToken();

            for (String companyId : StockCode.CODES) {
                try {
                    String json = fetchService.fetchPrice(token, companyId);


                    priceRedisService.savePrice(companyId, json);

                } catch (Exception e) {
                    System.out.println("❌ Error fetching " + companyId + ": " + e.getMessage());
                }

                try { Thread.sleep(300); } catch (InterruptedException ignore) {}
            }

        } finally {
            isRunning = false;
        }
    }
}
