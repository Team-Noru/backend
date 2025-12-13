package com.example.noru.news.rds.dto;

import com.example.noru.news.rds.entity.News;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsEsDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String publishedAt;
    private String publisher;

    // News 엔티티를 받아서 DTO로 변환하는 정적 메서드
    public static NewsEsDto from(News news) {
        return NewsEsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .description(news.getDescription())
                .content(news.getContent())
                .publishedAt(news.getPublishedAt()
                        .toLocalDate()
                        .toString())
                .publisher(news.getPublisher())
                .build();
    }
}