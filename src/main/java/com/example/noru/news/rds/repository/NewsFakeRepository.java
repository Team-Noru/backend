package com.example.noru.news.rds.repository;

import com.example.noru.company.rds.entity.Company;
import com.example.noru.news.rds.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public class NewsFakeRepository implements NewsRepository {

    private final List<News> data = new ArrayList<>();

    @PostConstruct
    public void init() {

        Company samsung = Company.builder()
                .stockCode("005930")
                .name("삼성전자")
                .build();

        Company skhynix = Company.builder()
                .stockCode("000660")
                .name("SK하이닉스")
                .build();

        Company kakao = Company.builder()
                .stockCode("035720")
                .name("카카오")
                .build();

        data.add(
                News.builder()
                        .id(1L)
                        .company(samsung)
                        .title("삼성전자, 차세대 AI 반도체 양산 본격화")
                        .description("삼성전자가 차세대 AI 반도체 양산 단계에 들어갔다.")
                        .content("...")
                        .publishedAt("2025-11-30")
                        .author("홍길동")
                        .contentUrl("https://news.example.com/1")
                        .thumbnailUrl("https://img.example.com/thumb1.jpg")
                        .publisher("한국경제")
                        .build()
        );

        data.add(
                News.builder()
                        .id(2L)
                        .company(skhynix)
                        .title("SK하이닉스, HBM5 개발 성공")
                        .description("...")
                        .content("...")
                        .publishedAt("2025-11-29")
                        .author("이기자")
                        .contentUrl("https://news.example.com/2")
                        .thumbnailUrl("https://img.example.com/thumb2.jpg")
                        .publisher("조선일보")
                        .build()
        );

        data.add(
                News.builder()
                        .id(3L)
                        .company(kakao)
                        .title("카카오, 플랫폼 구조 혁신 발표")
                        .description("...")
                        .content("...")
                        .publishedAt("2025-11-27")
                        .author("정기자")
                        .contentUrl("https://news.example.com/3")
                        .thumbnailUrl("https://img.example.com/thumb3.jpg")
                        .publisher("연합뉴스")
                        .build()
        );
    }

    // ------------------------------------------
    // Fake Repository 메서드 구현 (JPA와 형식 맞춤)
    // ------------------------------------------

    @Override
    public List<News> findAll() {
        return data;
    }

    @Override
    public Optional<News> findById(Long id) {
        return data.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<News> findByPublishedAt(String publishedAt) {

        return data.stream()
                .filter(n -> n.getPublishedAt().equals(publishedAt))
                .toList();
    }
}
