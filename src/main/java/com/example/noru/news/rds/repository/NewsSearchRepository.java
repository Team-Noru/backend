package com.example.noru.news.rds.repository;

import com.example.noru.news.es.NewsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NewsSearchRepository
        extends ElasticsearchRepository<NewsDocument, Long> {

    List<NewsDocument> findByTitleContainingOrContentContaining(
            String title,
            String content
    );
}