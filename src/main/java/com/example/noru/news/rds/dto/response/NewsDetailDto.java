package com.example.noru.news.rds.dto.response;

import com.example.noru.company.graph.dto.RelatedCompanyDto;
import com.example.noru.company.rds.entity.Company;
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
        List<CompanySentimentDto> companies,
        List<RelatedCompanyDto> related
) {
    public static NewsDetailDto fromEntity(
            News news,
            Company mainCompany,
            List<CompanySentimentDto> companies,
            List<RelatedCompanyDto> related
    ) {
        List<String> images = news.getImages().stream()
                .map(NewsImage::getImageUrl)
                .toList();

        return new NewsDetailDto(
                news.getId(),
                news.getTitle(),
                news.getDescription(),
                news.getContent(),
                news.getPublishedAt()
                        .toLocalDate()
                        .toString(),
                news.getAuthor(),
                news.getContentUrl(),
                news.getThumbnailUrl(),
                images,
                news.getPublisher(),
                mainCompany.getStockCode(),
                companies,
                related
        );
    }
}
