package com.example.noru.news.rds.dto;

import com.example.noru.news.rds.entity.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class S3NewsDto {
    private String title;
    private String content;
    private String url;
    private String publisher;
    private String publishedAt;
    private String companyCode; // 관련 종목 코드 (예: 005930)

    // DTO -> Entity 변환 메서드
    public News toEntity() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return News.builder()
                .title(this.title)
                .content(this.content)
                .contentUrl(this.url)
                .publisher(this.publisher)
                .publishedAt(LocalDateTime.parse(this.publishedAt, formatter))
                .companyId(this.companyCode)
                .build();
    }
}
