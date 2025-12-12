package com.example.noru.news.rds.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_images")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
}
