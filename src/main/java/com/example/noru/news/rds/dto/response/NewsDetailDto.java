package com.example.noru.news.rds.dto.response;

import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.entity.NewsImage;

import java.util.List;

public record NewsDetailDto(
        Long id,
        String title,
        String description,
        String content,
        String publishedAt,
        String author,
        String contentUrl,
        String thumbnailUrl,
        List<String> imageUrl,
        String publisher,
        String companyId,
        List<CompanySentimentDto> companies
) {
    public static NewsDetailDto fromEntity(News news) {
        List<String> images = news.getImages().stream()
                .map(NewsImage::getImageUrl)
                .toList();

        List<CompanySentimentDto> companySentimentDtos = news.getCompanySentiments().stream()
                .map(cs -> new CompanySentimentDto(
                        cs.getCompany().getCompanyId(),
                        cs.getCompany().getName(),
                        cs.getCompany().isDomestic(),
                        cs.getCompany().isListed(),
                        cs.getSentiment()
                ))
                .toList();

        return new NewsDetailDto(
                news.getId(),
                news.getTitle(),
                news.getDescription(),
                news.getContent(),
                news.getPublishedAt(),
                news.getAuthor(),
                news.getContentUrl(),
                news.getThumbnailUrl(),
                images,
                news.getPublisher(),
                news.getCompanyId(),
                companySentimentDtos
        );
    }
}
