package com.example.noru.company.graph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RelatedCompanyDto {

    private String companyId;
    private String name;
    private boolean isListed;
    private boolean isDomestic;
    private String sentiment;
    private List<TagDto> tags;
}