package com.example.noru.company.graph.dto;

public record TagDto(
        String label,
        String direction,
        Long newsId,
        String relReason
) {}


