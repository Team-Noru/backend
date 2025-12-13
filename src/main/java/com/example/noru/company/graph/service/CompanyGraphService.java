package com.example.noru.company.graph.service;

import com.example.noru.company.graph.dto.CompanyGraphResponseDto;
import com.example.noru.company.graph.dto.RelatedCompanyBuilder;
import com.example.noru.company.graph.dto.TagDto;
import com.example.noru.company.graph.node.CompanyGraphEntity;
import com.example.noru.company.graph.relationship.CompanyGraphRelation;
import com.example.noru.company.graph.repository.CompanyGraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
@Service
@RequiredArgsConstructor
public class CompanyGraphService {

    private final CompanyGraphRepository companyGraphRepository;

    public CompanyGraphResponseDto getCompanyGraph(String ticker) {

        CompanyGraphEntity root = companyGraphRepository
                .findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("기업 없음"));

        Map<String, RelatedCompanyBuilder> relatedMap = new LinkedHashMap<>();

        // 🔥 OUT + IN 합침
        List<CompanyGraphRelation> relations =
                Stream.concat(
                        root.getOutgoing().stream(),
                        root.getIncoming().stream()
                ).toList();

        for (CompanyGraphRelation relation : relations) {

            CompanyGraphEntity target = relation.getInvestor();

            String companyKey = resolveCompanyKey(target);

            relatedMap.computeIfAbsent(companyKey, key -> {

                boolean isListed =
                        target.getTicker() != null && !target.getTicker().isBlank();

                boolean isDomestic =
                        target.getCountry() == null
                                || "Korea".equalsIgnoreCase(target.getCountry());

                return new RelatedCompanyBuilder(
                        companyKey,
                        target.getName(),
                        isDomestic,
                        isListed
                );
            });

            relatedMap.get(companyKey).addTag(
                    new TagDto(
                            relation.getRelType(),
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

    /**
     * ✅ 핵심: Map key는 절대 겹치면 안 된다
     */
    private String resolveCompanyKey(CompanyGraphEntity entity) {

        if (entity.getTicker() != null && !entity.getTicker().isBlank()) {
            return entity.getTicker();          // 1️⃣ 상장사
        }

        if (entity.getCorpCode() != null && !entity.getCorpCode().isBlank()) {
            return entity.getCorpCode();        // 2️⃣ 비상장
        }

        return "neo4j:" + entity.getTicker();       // 3️⃣ 최후 방어선 (절대 유니크)
    }
}
