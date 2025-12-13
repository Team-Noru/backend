package com.example.noru.news.controller;

import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.news.rds.dto.response.NewsDetailDto;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.service.NewsSearchService;
import com.example.noru.news.rds.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;
    private final NewsSearchService newsSearchService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsListDto>>> getAllNews (
            @RequestParam(required = false) String date) {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_NEWS_LIST, newsService.getAllNews(date)));
    }


    @GetMapping("{newsId}")
    public ResponseEntity<ApiResponse<NewsDetailDto>> getNewsDetail (@PathVariable Long newsId) {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_NEWS_DETAIL, newsService.getNewsDetail(newsId)));
    }



    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NewsListDto>>> search(
            @RequestParam("q") String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_SEARCH,
                newsSearchService.search(keyword)
        ));
    }
}
