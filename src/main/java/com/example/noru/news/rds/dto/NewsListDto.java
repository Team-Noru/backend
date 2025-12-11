package com.example.noru.news.rds.dto;

import com.example.noru.news.rds.entity.News;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewsListDto {
    private Long id;
    private String title;
    private String description;
    private String publishedAt;
    private String publisher;
    private String thumbnailUrl;

    public static NewsListDto fromEntity(News n) {
        return NewsListDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .description(n.getDescription())
                .publishedAt(n.getPublishedAt())
                .publisher(n.getPublisher())
                .thumbnailUrl(n.getThumbnailUrl())
                .build();
    }
}
