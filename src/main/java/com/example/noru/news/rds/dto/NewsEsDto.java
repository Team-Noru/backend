package com.example.noru.news.rds.dto;

import com.example.noru.news.rds.entity.News;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsEsDto {
    private Long id;
    private String title;
    private String description;
    private String content;

    // String 대신 LocalDateTime 사용 추천 (시간 정보 유지)
    private LocalDateTime publishedAt;

    private String publisher;

    // [중요] 기업 매핑을 위해 반드시 필요!
    private String companyId;

    private String thumbnailUrl;

    // News 엔티티를 받아서 DTO로 변환하는 정적 메서드
    public static NewsEsDto from(News news) {
        return NewsEsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .description(news.getDescription())
                .content(news.getContent())
                .publishedAt(news.getPublishedAt()) // 시간 정보까지 그대로 전달
                .publisher(news.getPublisher())
                .companyId(news.getCompanyId())
                .thumbnailUrl(news.getThumbnailUrl())
                .build();
    }
}