package com.example.noru.company.graph.dto;

import java.util.ArrayList;
import java.util.List;

public class RelatedCompanyBuilder {

    private final String companyId;
    private final String name;
    private final boolean isDomestic;
    private final boolean isListed;
    private final List<TagDto> tags = new ArrayList<>();

    public RelatedCompanyBuilder(
            String companyId,
            String name,
            boolean isDomestic,
            boolean isListed
    ) {
        this.companyId = companyId;
        this.name = name;
        this.isDomestic = isDomestic;
        this.isListed = isListed;
    }

    public void addTag(TagDto tag) {
        tags.add(tag);
    }

    public RelatedCompanyDto build() {
        return new RelatedCompanyDto(
                companyId,
                name,
                isDomestic,
                isListed,
                tags
        );
    }
}
