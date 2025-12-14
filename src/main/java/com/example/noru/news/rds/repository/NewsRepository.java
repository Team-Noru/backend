package com.example.noru.news.rds.repository;

import com.example.noru.news.rds.entity.News;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByPublishedAtBetweenOrderByPublishedAtDesc(
            LocalDateTime start,
            LocalDateTime end
    );


    List<News> findByCompanyIdOrderByPublishedAtDesc(String companyId);

    List<News> findAllByOrderByPublishedAtDesc();

}
