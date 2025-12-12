package com.example.noru.news.rds.dto.response;

public record CompanySentimentDto(
        String stockCode,
        String name,
        boolean isDomestic,
        boolean isListed,
        String sentiment
) {}