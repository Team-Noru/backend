package com.example.noru.news.rds.service;

import com.example.noru.common.exception.NewsException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.graph.dto.RelatedCompanyBuilder;
import com.example.noru.company.graph.dto.RelatedCompanyDto;
import com.example.noru.company.graph.service.CompanyGraphService;
import com.example.noru.company.rds.entity.Company;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.news.rds.dto.NewsEsDto; // 아까 만든 DTO import 확인!
import com.example.noru.news.rds.dto.response.CompanySentimentDto;
import com.example.noru.news.rds.dto.response.NewsDetailDto;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.entity.OutboxEvent; // Outbox 엔티티 import
import com.example.noru.news.rds.repository.NewsRepository;
import com.example.noru.news.rds.repository.OutboxEventRepository; // Outbox 리포지토리 import
import com.example.noru.price.config.PriceParsingConfig;
import com.example.noru.price.dto.PriceDto;
import com.example.noru.price.service.PriceRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final CompanyRepository companyRepository;
    private final PriceRedisService priceRedisService;
    private final CompanyGraphService companyGraphService;

    // [추가됨] Outbox 패턴을 위한 의존성 주입
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    // =================================================================
    // [추가된 메서드] 뉴스 저장 + Elasticsearch 동기화 이벤트 발행
    // =================================================================
    @Transactional
    public Long saveNews(News news) {
        // 1. MySQL에 뉴스 원본 저장
        News savedNews = newsRepository.save(news);

        // 2. Elasticsearch용 DTO로 변환
        NewsEsDto esDto = NewsEsDto.from(savedNews);

        // 3. DTO를 JSON 문자열로 변환
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(esDto);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 에러", e);
            throw new RuntimeException("JSON 변환 오류", e);
        }

        // 4. Outbox 테이블에 이벤트 저장 (타입: NEWS)
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("NEWS")       // 👈 핵심: 뉴스 데이터임
                .aggregateId(savedNews.getId())
                .eventType("CREATED")
                .payload(payload)            // JSON 데이터
                .status("PENDING")
                .build();

        outboxEventRepository.save(event);

        log.info("뉴스 저장 및 검색 엔진 동기화 요청 완료: ID {}", savedNews.getId());
        return savedNews.getId();
    }

    // =================================================================
    // [기존 코드 유지] 아래는 작성자님이 원래 만드신 조회 로직들입니다.
    // =================================================================

    public List<NewsListDto> getAllNews(String date) {

        List<News> newsList;

        if (date == null || date.isBlank()) {
            newsList = newsRepository.findAllByOrderByPublishedAtDesc();
        } else {
            LocalDate localDate = LocalDate.parse(date);
            LocalDateTime start = localDate.atStartOfDay();
            LocalDateTime end = localDate.atTime(23, 59, 59);
            newsList = newsRepository.findByPublishedAtBetweenOrderByPublishedAtDesc(start, end);
        }

        if (newsList.isEmpty()) {
            throw new NewsException(ResponseCode.NEWS_NOT_FOUND);
        }

        List<String> companyIds = newsList.stream()
                .map(News::getCompanyId)
                .filter(id -> id != null)
                .distinct()
                .toList();

        List<Company> companies = companyRepository.findAllById(companyIds);


        var companyMap = companies.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                c -> c.getId().toString(),
                                Company::getStockCode
                        )
                );


        return newsList.stream()
                .map(news -> {
                    String stockCode = companyMap.get(news.getCompanyId());


                    return NewsListDto.fromEntity(news, stockCode);
                })
                .toList();
    }


    public List<NewsListDto> getNewsByCompanyId(String companyId) {

        Company company = companyRepository.findByStockCode(companyId)
                .orElseThrow(() -> new NewsException(ResponseCode.COMPANY_NOT_FOUND));

        Long id = company.getId();
        String stockCode = company.getStockCode();

        List<News> newsList =
                newsRepository.findByCompanyIdOrderByPublishedAtDesc(String.valueOf(id));

        if (newsList.isEmpty()) {
            throw new NewsException(ResponseCode.NEWS_NOT_FOUND);
        }

        return newsList.stream()
                .map(news -> NewsListDto.fromEntity(news, stockCode))
                .toList();
    }

    public NewsDetailDto getNewsDetail(Long newsId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        Company mainCompany = null;
        if (news.getCompanyId() != null) {
            mainCompany = companyRepository
                    .findById(news.getCompanyId())
                    .orElse(null);
        }

        List<String> sentimentCompanyIds = news.getCompanySentiments().stream()
                .map(cs -> cs.getCompanyId())
                .filter(id -> id != null)
                .distinct()
                .toList();

        Map<String, Company> companyMap =
                companyRepository.findAllById(sentimentCompanyIds)
                        .stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        c -> c.getId().toString(),
                                        c -> c
                                )
                        );

        List<CompanySentimentDto> sentiments =
                news.getCompanySentiments().stream()
                        .map(cs -> {

                            Company company = companyMap.get(cs.getCompanyId());

                            if (company == null) {
                                return new CompanySentimentDto(
                                        null, null, false, false,
                                        cs.getSentiment(),
                                        -1, 0, 0
                                );
                            }

                            String stockCode = company.getStockCode();
                            String exchange = company.getExchange(); // null 이면 국내

                            PriceDto priceDto = new PriceDto(stockCode, -1, 0, 0.0);

                            String json = priceRedisService.get(exchange, stockCode);
                            if (json != null) {
                                priceDto = PriceParsingConfig.parsePrice(stockCode, json);
                            }

                            return new CompanySentimentDto(
                                    stockCode,
                                    company.getName(),
                                    company.isDomestic(),
                                    company.isListed(),
                                    cs.getSentiment(),
                                    priceDto.price(),
                                    priceDto.diffPrice(),
                                    priceDto.diffRate()
                            );
                        })
                        .toList();

        List<RelatedCompanyDto> related = List.of();

        if (mainCompany != null && mainCompany.getStockCode() != null) {

            related = companyGraphService
                    .getRelatedCompaniesForNews(mainCompany.getStockCode())
                    .stream()
                    .map(RelatedCompanyBuilder::build)
                    .toList();
        }


        return NewsDetailDto.fromEntity(news, mainCompany, sentiments, related);
    }
}