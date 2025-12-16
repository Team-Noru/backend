package com.example.noru.company.graph.service;

import com.example.noru.common.exception.CompanyException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.graph.dto.CompanyGraphResponseDto;
import com.example.noru.company.graph.dto.RelatedCompanyBuilder;
import com.example.noru.company.graph.dto.TagDto;
import com.example.noru.company.graph.node.CompanyGraphEntity;
import com.example.noru.company.graph.relationship.CompanyGraphRelation;
import com.example.noru.company.graph.repository.CompanyGraphRepository;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.price.dto.PriceDto;
import com.example.noru.price.service.PriceGraphService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyGraphService {

    private final CompanyGraphRepository companyGraphRepository;
    private final CompanyRepository companyRepository;
    private final PriceGraphService priceGraphService;
    private final ObjectMapper objectMapper;


    public CompanyGraphResponseDto getCompanyGraph(String ticker) {

        CompanyGraphEntity root = companyGraphRepository
                .findByTicker(ticker)
                .orElseThrow(() -> new CompanyException(ResponseCode.COMPANY_RELATION_NOT_FOUND));

        PriceDto priceDto = priceGraphService.getPrice(ticker);

        Map<String, RelatedCompanyBuilder> relatedMap = new LinkedHashMap<>();

        root.getOutgoingRelations().forEach(relation ->
                processRelation(relation, relatedMap, true)
        );


        root.getIncomingRelations().forEach(relation ->
                processRelation(relation, relatedMap, false)
        );


        return new CompanyGraphResponseDto(
                root.getTicker(),
                root.getName(),
                root.isListed(),
                priceDto.price(),
                priceDto.diffPrice(),
                priceDto.diffRate(),
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

    @Transactional(readOnly = true)
    public List<RelatedCompanyBuilder> getRelatedCompaniesForNews(String ticker) {

        CompanyGraphEntity root = companyGraphRepository
                .findByTicker(ticker)
                .orElseThrow(() -> new CompanyException(ResponseCode.COMPANY_RELATION_NOT_FOUND));

        Map<String, RelatedCompanyBuilder> relatedMap = new LinkedHashMap<>();

        // OUTGOING
        root.getOutgoingRelations().stream()
                .filter(r -> r.getExtraJson() != null && !r.getExtraJson().isBlank())
                .forEach(r -> processRelation(r, relatedMap, true));

        // INCOMING
        root.getIncomingRelations().stream()
                .filter(r -> r.getExtraJson() != null && !r.getExtraJson().isBlank())
                .forEach(r -> processRelation(r, relatedMap, false));

        return relatedMap.values().stream().toList();
    }


    private String resolveLabel(CompanyGraphRelation relation) {
        return relation.getRelType();
    }

    private String resolveDirectionLabel(boolean isOutgoing) {
        return isOutgoing ? "OUT" : "IN";
    }

    private String resolveRelReason(CompanyGraphRelation relation) {

        if (relation.getExtraJson() != null && !relation.getExtraJson().isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(relation.getExtraJson());
                JsonNode reasonNode = root.get("reason");

                if (reasonNode != null && !reasonNode.isNull()) {
                    return reasonNode.asText();
                }
            } catch (Exception e) {
                log.warn("Failed to parse extra_json: {}", relation.getExtraJson(), e);
            }
        }

        return relation.getRelReason();
    }

    private String buildIpoDilutionReason(CompanyGraphRelation relation) {

        if (relation.getExtraJson() == null || relation.getExtraJson().isBlank()) {
            return relation.getRelReason();
        }

        try {
            JsonNode root = objectMapper.readTree(relation.getExtraJson());

            double prevRatio = root.path("prev_ratio").asDouble();
            double currRatio = root.path("curr_ratio").asDouble();
            double dilutionPp = root.path("dilution_pp").asDouble();
            double dilutionPct = root.path("dilution_pct").asDouble();
            String cause = root.path("change_cause").asText("");

            return String.format(
                    "IPO 전 지분율: %.2f%%\n" +
                    "IPO 후 지분율: %.2f%%\n" +
                    "희석폭: -%.2f%%p\n" +
                    "희석률: -%.2f%%\n" +
                    "변동 사유: %s",
                    prevRatio,
                    currRatio,
                    dilutionPp,
                    dilutionPct,
                    cause.isBlank() ? "정보 없음" : cause
            );


        } catch (Exception e) {
            log.warn("Failed to build IPO_DILUTION reason: {}", relation.getExtraJson(), e);
            return relation.getRelReason();
        }
    }

    private String buildCapitalIncreaseReason(CompanyGraphRelation relation) {

        if (relation.getExtraJson() == null || relation.getExtraJson().isBlank()) {
            return relation.getRelReason();
        }

        try {
            JsonNode root = objectMapper.readTree(relation.getExtraJson());

            String relationRaw = root.path("relation_raw").asText("관계자");
            String assignedSharesRaw = root.path("assigned_shares_raw").asText();
            String reason = root.path("reason").asText("");

            StringBuilder sb = new StringBuilder();
            sb.append(relationRaw)
                    .append("가 유상증자에 참여하여 ");

            if (!assignedSharesRaw.isBlank()) {
                sb.append("신주 ").append(assignedSharesRaw).append("주를 배정받음");
            } else {
                sb.append("신주를 배정받음");
            }

            if (!reason.isBlank()) {
                sb.append("\n(참여 사유: ").append(reason).append(")");
            }

            return sb.toString();

        } catch (Exception e) {
            log.warn("Failed to build CAPITAL_INCREASE reason: {}", relation.getExtraJson(), e);
            return relation.getRelReason();
        }
    }



    private void processRelation(
            CompanyGraphRelation relation,
            Map<String, RelatedCompanyBuilder> relatedMap,
            boolean isOutgoing
    ) {
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

        String label = resolveLabel(relation);

        String reason;
        if ("IPO_DILUTION".equals(label)) {
            reason = buildIpoDilutionReason(relation);
        } else if ("CAPITAL_INCREASE".equals(label)) {
            reason = buildCapitalIncreaseReason(relation);
        } else {
            reason = resolveRelReason(relation);
        }


        relatedMap.get(companyKey).addTag(
                new TagDto(
                        label,
                        resolveDirectionLabel(isOutgoing),
                        relation.getNewsId() != null
                                ? Long.parseLong(relation.getNewsId())
                                : null,
                        reason
                )
        );
    }


}
