package com.example.noru.price.service;

import com.example.noru.company.rds.service.CompanyService;
import com.example.noru.price.domain.StockCode;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceScheduler {

    private final TokenService tokenService;
    private final PriceFetchService fetchService;
    private final PriceRedisService priceRedisService;
    private final CompanyService companyService;

    private boolean isRunning = false;

    // 초당 3.3개 호출 → 약 0.3초 간격
    private final RateLimiter rateLimiter = RateLimiter.create(3.3);

    // 09:00 ~ 14:59
    @Scheduled(cron = "0 * 9-14 * * MON-FRI")
    public void updateMorning() {
        runScheduler();
    }

    // 15:00 ~ 15:29
    @Scheduled(cron = "0 0-29 15 * * MON-FRI")
    public void updateAfternoon() {
        runScheduler();
    }

    public void runScheduler() {

        if (isRunning) return;
        isRunning = true;

        try {
            String token = tokenService.getAccessToken();
            List<String> stockCodes = companyService.getDomesticListedCompanies();

            for (String companyId : stockCodes) {

                // ⏱ API 호출 속도 조절 (0.3초 간격)
                rateLimiter.acquire();

                try {
                    String json = fetchService.fetchPrice(token, companyId);
                    priceRedisService.savePrice(companyId, json);

                } catch (Exception e) {
                    System.out.println("❌ Error fetching " + companyId + ": " + e.getMessage());
                }
            }

        } finally {
            isRunning = false;
        }
    }
}
