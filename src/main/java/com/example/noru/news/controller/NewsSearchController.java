package com.example.noru.news.controller;

import com.example.noru.company.document.CompanyDocument;
import com.example.noru.company.rds.repository.CompanySearchRepository;
import com.example.noru.news.document.NewsDocument;
import com.example.noru.news.rds.dto.SearchResponseDto;
import com.example.noru.news.rds.service.NewsSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NewsSearchController {

    private final NewsSearchService newsSearchService;
    private final CompanySearchRepository companySearchRepository;

    // 1. [통합 검색] 뉴스(제목+내용+기업명) + 기업(이름+코드) 동시에 검색
    // 사용법: GET /api/news/total?keyword=삼성
    @GetMapping("/total")
    public ResponseEntity<SearchResponseDto> searchTotal(@RequestParam("keyword") String keyword) {

        // 1-1. 뉴스 검색 (제목 + 본문 + 기업명)
        List<NewsDocument> newsResult = newsSearchService.searchGlobal(keyword);

        // 1-2. 기업 검색 (기업명 + 종목코드)
        List<CompanyDocument> companyResult = companySearchRepository.searchByNameOrCode(keyword);

        // 1-3. 결과 묶어서 반환
        SearchResponseDto response = SearchResponseDto.builder()
                .news(newsResult)
                .companies(companyResult)
                .build();

        return ResponseEntity.ok(response);
    }

    // 2. [뉴스 검색] 뉴스만 검색하고 싶을 때
    // 사용법: GET /api/news/search?keyword=반도체
    @GetMapping("/search")
    public ResponseEntity<List<NewsDocument>> searchNews(@RequestParam("keyword") String keyword) {
        List<NewsDocument> result = newsSearchService.searchGlobal(keyword);
        return ResponseEntity.ok(result);
    }

    // 3. [기업별 뉴스] 특정 종목코드의 뉴스만 보고 싶을 때
    // 사용법: GET /api/news/company/005930
    @GetMapping("/company")
    public ResponseEntity<List<NewsDocument>> searchByCompany(@RequestParam("code") String code) {
        List<NewsDocument> result = newsSearchService.searchByCompanyCode(code);
        return ResponseEntity.ok(result);
    }
}
