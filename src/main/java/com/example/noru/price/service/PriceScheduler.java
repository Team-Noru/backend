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
    private final PriceFetchService domesticFetchService;
    private final OverseasPriceFetchService overseasFetchService;
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

            /* =========================
             * 1️⃣ 국내 상장 기업
             * ========================= */
            List<String> domesticStockCodes =
                    companyService.getDomesticListedCompanies(); // exchange = null

            for (String stockCode : domesticStockCodes) {
                rateLimiter.acquire();

                try {
                    String json = domesticFetchService.fetchPrice(token, stockCode);
                    priceRedisService.saveDomestic(stockCode, json);

                } catch (Exception e) {
                    System.out.println("❌ Domestic fetch error " + stockCode + ": " + e.getMessage());
                }
            }

            /* =========================
             * 2️⃣ 해외 상장 기업
             * ========================= */
            companyService.getOverseasListedCompanies() // exchange != null
                    .forEach(company -> {

                        rateLimiter.acquire();

                        try {
                            String json = overseasFetchService.fetch(
                                    token,
                                    company.getExchange(),      // BAY, BAQ, HKS …
                                    company.getStockCode()
                            );

                            priceRedisService.saveOverseas(
                                    company.getExchange(),
                                    company.getStockCode(),
                                    json
                            );

                        } catch (Exception e) {
                            System.out.println(
                                    "❌ Overseas fetch error "
                                            + company.getExchange() + ":" + company.getStockCode()
                                            + " → " + e.getMessage()
                            );
                        }
                    });

        } finally {
            isRunning = false;
        }
    }
}
