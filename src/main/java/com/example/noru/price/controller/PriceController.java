package com.example.noru.price.controller;

import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.rds.entity.Company;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.price.config.PriceParsingConfig;
import com.example.noru.price.domain.StockCode;
import com.example.noru.price.dto.MajorDto;
import com.example.noru.price.dto.PriceDto;
import com.example.noru.price.service.PriceRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/price")
public class PriceController {

    private final PriceRedisService priceRedisService;
    private final CompanyRepository companyRepository;

//    @GetMapping("/{companyId}")
//    public PriceDto getPrice(@PathVariable String companyId) {
//        String json = priceRedisService.get(companyId);
//        return PriceParsingConfig.parsePrice(companyId, json);
//    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MajorDto>>> getMajorStockPrices() {

        List<MajorDto> result =
                StockCode.CODES.stream()
                        .map(stockCode -> {

                            // 1️⃣ 회사 조회
                            Company company = companyRepository
                                    .findAllByStockCode(stockCode)
                                    .stream()
                                    .findFirst()
                                    .orElse(null);

                            if (company == null) {
                                return new MajorDto(
                                        stockCode,
                                        null,
                                        -1,
                                        0,
                                        0.0
                                );
                            }

                            // 2️⃣ Redis 조회 (exchange 기준)
                            String json = priceRedisService.get(
                                    company.getExchange(),   // null이면 DOMESTIC
                                    company.getStockCode()
                            );

                            // 3️⃣ 가격 파싱
                            PriceDto price = json != null
                                    ? PriceParsingConfig.parsePrice(stockCode, json)
                                    : new PriceDto(stockCode, -1, 0, 0.0);

                            // 4️⃣ 응답 DTO
                            return new MajorDto(
                                    stockCode,
                                    company.getName(),
                                    price.price(),
                                    price.diffPrice(),
                                    price.diffRate()
                            );
                        })
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_PRICE, result)
        );
    }

}