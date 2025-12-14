package com.example.noru.price.dto;

public record MajorDto (
        String companyId,
        String name,
        long price,          // stck_prpr
        long diffPrice,      // prdy_vrss
        double diffRate // prdy_ctrt
) {}
