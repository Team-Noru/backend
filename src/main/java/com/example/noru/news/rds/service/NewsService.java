package com.example.noru.news.rds.service;

import com.example.noru.common.exception.NewsException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.news.rds.dto.response.CompanySentimentDto;
import com.example.noru.news.rds.dto.response.NewsDetailDto;
import com.example.noru.news.rds.dto.response.NewsListDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.repository.NewsRepository;
import com.example.noru.price.config.PriceParsingConfig;
import com.example.noru.price.service.PriceRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final PriceRedisService priceRedisService;

    public List<NewsListDto> getAllNews(String date) {
        List<NewsListDto> result;

        if (date == null || date.isBlank()) {
            result = newsRepository.findAll().stream()
                    .map(NewsListDto::fromEntity)
                    .toList();
        } else {
            LocalDate localDate = LocalDate.parse(date);

            LocalDateTime start = localDate.atStartOfDay();
            LocalDateTime end = localDate.atTime(23, 59, 59);

            result = newsRepository.findByPublishedAtBetween(start, end)
                    .stream()
                    .map(NewsListDto::fromEntity)
                    .toList();
        }

        if (result.isEmpty()) {
            throw new NewsException(ResponseCode.NEWS_NOT_FOUND);
        }

        return result;
    }

    public List<NewsListDto> getNewsByCompanyId(String companyId) {

        List<News> news = newsRepository.findByCompanyIdOrderByPublishedAtDesc(companyId);

        if (news.isEmpty()) {
            throw new NewsException(ResponseCode.NEWS_NOT_FOUND);
        }

        return news.stream()
                .map(NewsListDto::fromEntity)
                .toList();
    }


    public NewsDetailDto getNewsDetail(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ResponseCode.NEWS_NOT_FOUND));

        List<CompanySentimentDto> companies =
                news.getCompanySentiments().stream()
                        .map(cs -> {
                            String companyId = cs.getCompany().getStockCode();
                            String json = priceRedisService.get(companyId);
                            long price = PriceParsingConfig.parsePrice(companyId, json).price();

                            return new CompanySentimentDto(
                                    companyId,
                                    cs.getCompany().getName(),
                                    cs.getCompany().isDomestic(),
                                    cs.getCompany().isListed(),
                                    cs.getSentiment(),
                                    price
                            );
                        })
                        .toList();

        return NewsDetailDto.fromEntity(news, companies);
    }
}

