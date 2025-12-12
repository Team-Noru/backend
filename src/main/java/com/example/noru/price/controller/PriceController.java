package com.example.noru.price.controller;

import com.example.noru.price.config.PriceParsingConfig;
import com.example.noru.price.dto.PriceDto;
import com.example.noru.price.service.PriceRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class PriceController {

    private final PriceRedisService priceRedisService;

    @GetMapping("/{companyId}/price")
    public PriceDto getPrice(@PathVariable String code) {
        String json = priceRedisService.get(code);
        return PriceParsingConfig.parsePrice(code, json);
    }
}