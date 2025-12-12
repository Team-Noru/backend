package com.example.noru.news.rds.dto.response;

import com.example.noru.news.rds.entity.News;

public record NewsListDto (
    Long id,
    String title,
    String description,
    String publishedAt,
    String publisher,
    String thumbnailUrl
) {
    public static NewsListDto fromEntity(News news) {
        return new NewsListDto(
                news.getId(),
                news.getTitle(),
                news.getDescription(),
                news.getPublishedAt(),
                news.getPublisher(),
                news.getThumbnailUrl()
        );
    }
}