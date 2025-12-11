package com.example.noru.news.rds.entity;

import com.example.noru.company.rds.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String content;

    private String publishedAt;

    private String thumbnailUrl;

    private String contentUrl;

    private String publisher;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_code")  // FK: news.stock_code
    private Company company;
}