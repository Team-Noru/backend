package com.example.noru.news.rds.dto.response;

import com.example.noru.news.rds.entity.News;

import java.time.LocalDateTime;

public record NewsListDto (
    Long id,
    String companyId,
    String title,
    String description,
    String publishedAt,
    String publisher,
    String thumbnailUrl
) {
    public static NewsListDto fromEntity(News news, String stockCode) {
        return new NewsListDto(
                news.getId(),
                stockCode,
                news.getTitle(),
                news.getDescription(),
                news.getPublishedAt()
                        .toLocalDate()
                        .toString(),
                news.getPublisher(),
                news.getThumbnailUrl()
        );
    }
}