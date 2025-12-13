package com.example.noru.news.rds.service;

import com.example.noru.news.es.NewsDocument;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.repository.NewsSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSearchService {

    private final NewsSearchRepository newsSearchRepository;

    public List<NewsListDto> search(String keyword) {

        List<NewsDocument> docs =
                newsSearchRepository.findByTitleContainingOrContentContaining(
                        keyword, keyword
                );

        return docs.stream()
                .map(NewsListDto::fromDocument)
                .toList();
    }
}
