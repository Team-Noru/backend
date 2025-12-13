package com.example.noru.company.graph.repository;

import com.example.noru.company.graph.dto.RelatedCompanyDto;
import com.example.noru.company.graph.dto.TagDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyGraphRepository {

    public List<TagDto> findTagsByCompanyId(String companyId) {
        if ("005930".equals(companyId)) {
            return List.of(
                    new TagDto(1L, "반도체"),
                    new TagDto(2L, "AI")
            );
        }
        return List.of();
    }

    public List<RelatedCompanyDto> findRelatedCompanies(String companyId) {
        if ("005930".equals(companyId)) {
            return List.of(
                    new RelatedCompanyDto(
                            "055550",
                            "신한지주",
                            true,
                            true,
                            "positive",
                            List.of(new TagDto(3L, "금융"))
                    ),
                    new RelatedCompanyDto(
                            "035720",
                            "카카오",
                            true,
                            true,
                            "slightlyPositive",
                            List.of(new TagDto(4L, "플랫폼"))
                    ),
                    new RelatedCompanyDto(
                            "035420",
                            "네이버",
                            true,
                            true,
                            "neutral",
                            List.of(new TagDto(5L, "플랫폼"))
                    )
            );
        }
        return List.of();
    }
}
