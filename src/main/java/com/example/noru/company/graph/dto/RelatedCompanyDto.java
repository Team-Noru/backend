package com.example.noru.company.graph.dto;

import java.util.List;

public record RelatedCompanyDto(
        String companyId,
        String name,
        boolean isDomestic,
        boolean isListed,
        List<TagDto> tags
) {}
