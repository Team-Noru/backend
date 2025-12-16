package com.example.noru.company.graph.dto;

import java.util.Map;

public record TagDto(
        String label,
        String direction,
        Long newsId,
        String relReason,
        Map<String, Object> extra
) {}


