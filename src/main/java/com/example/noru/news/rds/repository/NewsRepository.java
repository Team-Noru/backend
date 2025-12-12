package com.example.noru.news.rds.repository;

import com.example.noru.news.rds.entity.News;

import java.util.List;
import java.util.Optional;

public interface NewsRepository {
    List<News> findAll();

    Optional<News> findById(Long id);

    List<News> findByPublishedAt(String date);

    List<News> findByCompanyId(String companyId);
}
