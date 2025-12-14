package com.example.noru.company.rds.dto;

import com.example.noru.company.rds.entity.Company;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyEsDto {
    private Long id;
    private String name;
    private String stockCode;
    private boolean isDomestic;
    private boolean isListed;

    public static CompanyEsDto from(Company company) {
        return CompanyEsDto.builder()
                .id(company.getId())
                .name(company.getName())
                .stockCode(company.getStockCode())
                .isDomestic(company.isDomestic())
                .isListed(company.isListed())
                .build();
    }
}
