package com.example.noru.news.rds.dto;

import com.example.noru.company.document.CompanyDocument;
import com.example.noru.news.document.NewsDocument;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponseDto {
    private List<NewsDocument> news;

    private List<CompanyDocument> companies;
}
