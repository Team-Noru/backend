package com.example.noru.news.controller;

import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.news.rds.dto.NewsListDto;
import com.example.noru.news.rds.service.NewsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsListDto>>> getAllNews(
            @RequestParam(required = false) String date) {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_NEWS_LIST, newsService.getAllNews(date)));
    }

}
