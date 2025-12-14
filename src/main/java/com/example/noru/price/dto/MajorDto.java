package com.example.noru.price.dto;

public record MajorDto (
        String code,
        String name,
        long price,          // stck_prpr
        long diffPrice,      // prdy_vrss
        double diffRate // prdy_ctrt
) {}
