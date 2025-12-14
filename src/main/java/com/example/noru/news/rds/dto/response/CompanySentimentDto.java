package com.example.noru.news.rds.dto.response;

public record CompanySentimentDto(
        String companyId,
        String companyName,
        boolean isDomestic,
        boolean isListed,
        String sentiment,
        long price,
        long diffPrice,
        double diffRate
) {}
