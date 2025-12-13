package com.example.noru.company.graph.service;

import com.example.noru.company.graph.dto.CompanyGraphResponseDto;
import com.example.noru.company.graph.dto.RelatedCompanyBuilder;
import com.example.noru.company.graph.dto.TagDto;
import com.example.noru.company.graph.node.CompanyGraphEntity;
import com.example.noru.company.graph.relationship.CompanyGraphRelation;
import com.example.noru.company.graph.repository.CompanyGraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyGraphService {

    private final CompanyGraphRepository companyGraphRepository;

    public CompanyGraphResponseDto getCompanyGraph(String ticker) {

        CompanyGraphEntity root = companyGraphRepository
                .findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("기업 없음"));

        Map<String, RelatedCompanyBuilder> relatedMap = new LinkedHashMap<>();

        for (CompanyGraphRelation relation : root.getRelations()) {

            CompanyGraphEntity target = relation.getInvestor();
            String companyKey = resolveCompanyKey(target);

            relatedMap.computeIfAbsent(companyKey, key -> {

                boolean isListed =
                        target.getTicker() != null && !target.getTicker().isBlank();

                boolean isDomestic =
                        target.getCountry() == null
                                || "Korea".equalsIgnoreCase(target.getCountry());

                return new RelatedCompanyBuilder(
                        target.getTicker(),
                        target.getName(),
                        isDomestic,
                        isListed
                );
            });

            // ✅ 여기만 바뀜
            String label =
                    relation.getNewsId() != null
                            ? "NEWS"
                            : relation.getRelType();

            relatedMap.get(companyKey).addTag(
                    new TagDto(
                            label,
                            relation.getNewsId() != null
                                    ? Long.parseLong(relation.getNewsId())
                                    : null,
                            relation.getRelReason()
                    )
            );
        }

        return new CompanyGraphResponseDto(
                root.getTicker(),
                root.getName(),
                root.isListed(),
                relatedMap.values()
                        .stream()
                        .map(RelatedCompanyBuilder::build)
                        .toList()
        );
    }

    private String resolveCompanyKey(CompanyGraphEntity entity) {
        if (entity.getTicker() != null && !entity.getTicker().isBlank()) {
            return entity.getTicker();
        }
        if (entity.getCorpCode() != null && !entity.getCorpCode().isBlank()) {
            return entity.getCorpCode();
        }
        return "neo4j:" + entity.getId();
    }
}
