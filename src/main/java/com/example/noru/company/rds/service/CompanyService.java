package com.example.noru.company.rds.service;

import com.example.noru.common.exception.CompanyException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.rds.dto.AnnouncementDto;
import com.example.noru.company.rds.dto.CompanyEsDto;
import com.example.noru.company.rds.dto.WordCloudDto;
import com.example.noru.company.rds.dto.WordDto;
import com.example.noru.company.rds.entity.Company;
import com.example.noru.company.rds.entity.WordCloud;
import com.example.noru.company.rds.repository.AnnouncementRepository;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.company.rds.repository.WordCloudRepository;
import com.example.noru.news.rds.entity.OutboxEvent;
import com.example.noru.news.rds.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final WordCloudRepository wordCloudRepository;
    private final AnnouncementRepository announcementRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public List<String> getDomesticListedCompanies() {
        return companyRepository.findValidCompanyIds();
    }

    @Transactional
    public Long saveCompany(Company company) {
        // 1. MySQL에 원본 데이터 저장
        Company savedCompany = companyRepository.save(company);

        // 2. Elasticsearch용 DTO로 변환
        CompanyEsDto esDto = CompanyEsDto.from(savedCompany);

        // 3. DTO를 JSON 문자열로 변환
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(esDto);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 에러", e);
            throw new RuntimeException("JSON 변환 오류", e);
        }

        // 4. Outbox 테이블에 이벤트 저장 (타입: COMPANY)
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("COMPANY") // 👈 핵심: 기업 데이터 식별
                .aggregateId(savedCompany.getId())
                .eventType("CREATED")
                .payload(payload)
                .status("PENDING")
                .build();

        outboxEventRepository.save(event);

        log.info("기업 저장 및 검색 엔진 동기화 요청 완료: {} ({})", savedCompany.getName(), savedCompany.getStockCode());
        return savedCompany.getId();
    }

    public List<AnnouncementDto> getAnnouncementsByCompany(String companyId) {

        List<AnnouncementDto> result = announcementRepository.findByCompanyIdOrderByPublishedAtDesc(companyId)
                .stream()
                .map(AnnouncementDto::fromEntity)
                .toList();

        if (result.isEmpty()) {
            throw new CompanyException(ResponseCode.ANNOUNCEMENT_NOT_FOUND);
        }

        return result;
    }

    public WordCloudDto getWordCloud(String companyId) {

        List<WordCloud> words = wordCloudRepository.findByCompanyId(companyId);

        if (words.isEmpty()) {
            throw new CompanyException(ResponseCode.WORD_CLOUD_NOT_FOUND);
        }

        List<WordDto> wordList = words.stream()
                .map(w -> new WordDto(
                        w.getText(),
                        w.getWeight(),
                        w.getType()
                ))
                .toList();

        return new WordCloudDto(companyId, wordList);
    }
}