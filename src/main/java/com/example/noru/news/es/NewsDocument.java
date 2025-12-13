package com.example.noru.news.es;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "news")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDocument {

    @Id
    private Long id;

    private String companyId;
    private String title;
    private String description;
    private String content;

    private String publisher;
    private LocalDateTime publishedAt;

    private String thumbnailUrl;

    private List<String> companyNames; // 검색용
}
