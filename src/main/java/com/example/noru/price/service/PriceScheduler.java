package com.example.noru.price.service;

import com.example.noru.company.rds.service.CompanyService;
import com.example.noru.price.config.ExecutorConfig;
import com.example.noru.price.domain.StockCode;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class PriceScheduler {

    private final TokenService tokenService;
    private final PriceFetchService domesticFetchService;
    private final OverseasPriceFetchService overseasFetchService;
    private final PriceRedisService priceRedisService;
    private final CompanyService companyService;
    private final ExecutorService priceExecutorService;

    private final RateLimiter rateLimiter = RateLimiter.create(3.3);
    private boolean isRunning = false;

    @Scheduled(cron = "0 * 8-14 * * MON-FRI")
    public void updateDomesticMorning() {
        runDomestic();
    }

    @Scheduled(cron = "0 0-29 15 * * MON-FRI")
    public void updateDomesticAfternoon() {
        runDomestic();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void updateOverseasDaily() {
        runOverseas();
    }

    private void runDomestic() {
        if (isRunning) return;
        isRunning = true;

        try {
            String token = tokenService.getAccessToken();
            List<String> stockCodes = companyService.getDomesticListedCompanies();

            for (String stockCode : stockCodes) {
                priceExecutorService.submit(() -> {

                    rateLimiter.acquire();

                    try {
                        String json = domesticFetchService.fetchPrice(token, stockCode);
                        priceRedisService.saveDomestic(stockCode, json);

                    } catch (Exception e) {
                        System.out.println(
                                "❌ Domestic fetch error " + stockCode + ": " + e.getMessage()
                        );
                    }
                });
            }

        } finally {
            isRunning = false;
        }
    }

    private void runOverseas() {

        try {
            String token = tokenService.getAccessToken();

            companyService.getOverseasListedCompanies()
                    .forEach(company -> {

                        rateLimiter.acquire();

                        try {
                            String json = overseasFetchService.fetch(
                                    token,
                                    company.getExchange(),
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
                            );
                        }
                    });

        } catch (Exception e) {
            System.out.println("❌ Overseas scheduler error: " + e.getMessage());
        }
    }
}
