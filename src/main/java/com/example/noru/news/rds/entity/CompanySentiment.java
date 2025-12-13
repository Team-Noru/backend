package com.example.noru.news.rds.entity;

import com.example.noru.company.rds.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_sentiment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CompanySentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sentiment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String sentiment;

}
