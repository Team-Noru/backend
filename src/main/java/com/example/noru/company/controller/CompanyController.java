package com.example.noru.company.controller;

import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.graph.dto.CompanyGraphResponseDto;
import com.example.noru.company.graph.node.CompanyGraphEntity;
import com.example.noru.company.graph.service.CompanyGraphService;
import com.example.noru.company.rds.dto.AnnouncementDto;
import com.example.noru.company.rds.service.AnnouncementService;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final AnnouncementService announcementService;
    private final NewsService newsService;
    private final CompanyGraphService graphService;

    @GetMapping("/{companyId}/announcement")
    public ResponseEntity<?> getAnnouncements(@PathVariable String companyId) {
        List<AnnouncementDto> result = announcementService.getAnnouncementsByCompany(companyId);

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_ANNOUNCEMENT, result));
    }

    @GetMapping("/{companyId}/news")
    public ResponseEntity<ApiResponse<List<NewsListDto>>> getNewsByCompanyId (@PathVariable String companyId) {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_NEWS_COMPANY, newsService.getNewsByCompanyId(companyId)));
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<ApiResponse<CompanyGraphResponseDto>> getGraph(@PathVariable String ticker) {
        log.info("Request ticker = {}", ticker);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_COMPANY_DETAIL, graphService.getCompanyGraph(ticker)));
    }
}
