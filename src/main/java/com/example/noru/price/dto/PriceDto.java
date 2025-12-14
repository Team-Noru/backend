package com.example.noru.price.dto;

public record PriceDto(
        String code,
        long price,          // stck_prpr
        long diffPrice,      // prdy_vrss
        double diffRate // prdy_ctrt
) {}