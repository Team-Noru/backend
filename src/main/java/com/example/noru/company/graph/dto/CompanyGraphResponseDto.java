package com.example.noru.company.graph.dto;

import java.util.List;

public record CompanyGraphResponseDto(
        String companyId,
        String name,
        boolean isListed,
        long price,
        long diffPrice,
        double diffRate,
        List<RelatedCompanyDto> related
) {}
