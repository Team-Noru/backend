package com.example.noru.news.rds.dto.response;

import com.example.noru.news.es.NewsDocument;
import com.example.noru.news.rds.entity.News;


public record NewsListDto (
    Long id,
    String companyId,
    String title,
    String description,
    String publishedAt,
    String publisher,
    String thumbnailUrl
) {
    public static NewsListDto fromEntity(News news) {
        return new NewsListDto(
                news.getId(),
                news.getCompanyId(),
                news.getTitle(),
                news.getDescription(),
                news.getPublishedAt()
                        .toLocalDate()
                        .toString(),
                news.getPublisher(),
                news.getThumbnailUrl()
        );
    }

    public static NewsListDto fromDocument(NewsDocument doc) {
        return new NewsListDto(
                doc.getId(),
                doc.getCompanyId(),   // ES에 없으면 null
                doc.getTitle(),
                doc.getDescription(),
                doc.getPublishedAt()
                        .toLocalDate()
                        .toString(),
                doc.getPublisher(),
                doc.getThumbnailUrl()
        );
    }
}