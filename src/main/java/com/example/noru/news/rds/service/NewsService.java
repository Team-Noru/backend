package com.example.noru.news.rds.service;

import com.example.noru.common.exception.NewsException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.news.rds.dto.response.NewsDetailDto;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public List<NewsListDto> getAllNews(String date) {
        List<NewsListDto> result;

        if (date == null || date.isBlank()) {
            result = newsRepository.findAll().stream()
                    .map(NewsListDto::fromEntity)
                    .toList();
        } else {
            result = newsRepository.findByPublishedAt(date).stream()
                    .map(NewsListDto::fromEntity)
                    .toList();
        }

        if (result.isEmpty()) {
            throw new NewsException(ResponseCode.NEWS_NOT_FOUND);
        }

        return result;
    }

    public NewsDetailDto getNewsDetail(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        return NewsDetailDto.fromEntity(news);
    }
}

