package com.example.noru.news.rds.repository;

import com.example.noru.company.rds.entity.Company;
import com.example.noru.news.rds.entity.CompanySentiment;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.entity.NewsImage;
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
                .companyId("005930")
                .name("삼성전자")
                .isDomestic(true)
                .isListed(true)
                .build();

        Company skhynix = Company.builder()
                .companyId("000660")
                .name("SK하이닉스")
                .isDomestic(true)
                .isListed(true)
                .build();

        Company kakao = Company.builder()
                .companyId("035720")
                .name("카카오")
                .isDomestic(true)
                .isListed(true)
                .build();

        News news1 = News.builder()
                .id(1L)
                .title("삼성전자, 차세대 AI 반도체 양산 본격화")
                .description("삼성전자가 차세대 AI 반도체 양산 단계에 들어갔다.")
                .content("삼성은 새로운 AI 가속기 칩을 대규모 양산한다고 밝혔다...")
                .publishedAt("2025-11-30")
                .author("홍길동")
                .contentUrl("https://news.example.com/1")
                .thumbnailUrl("https://img.example.com/thumb1.jpg")
                .publisher("한국경제")
                .companyId("005930")
                .build();


        news1.getImages().add(
                NewsImage.builder().news(news1).imageUrl("https://img.example.com/image1.jpg").build()
        );
        news1.getImages().add(
                NewsImage.builder().news(news1).imageUrl("https://img.example.com/image1-2.jpg").build()
        );


        news1.getCompanySentiments().add(
                CompanySentiment.builder()
                        .news(news1)
                        .company(samsung)
                        .sentiment("positive")
                        .build()
        );

        news1.getCompanySentiments().add(
                CompanySentiment.builder()
                        .news(news1)
                        .company(skhynix)
                        .sentiment("neutral")
                        .build()
        );

        data.add(news1);


        News news2 = News.builder()
                .id(2L)
                .title("SK하이닉스, HBM5 개발 성공")
                .description("SK하이닉스가 HBM5 개발에 성공했다.")
                .content("HBM5는 기존 대비 30% 이상 성능이 향상되었다...")
                .publishedAt("2025-11-29")
                .author("이기자")
                .contentUrl("https://news.example.com/2")
                .thumbnailUrl("https://img.example.com/thumb2.jpg")
                .publisher("조선일보")
                .companyId("000660")
                .build();

        news2.getImages().add(
                NewsImage.builder().news(news2).imageUrl("https://img.example.com/hbm.jpg").build()
        );

        news2.getCompanySentiments().add(
                CompanySentiment.builder()
                        .news(news2)
                        .company(skhynix)
                        .sentiment("positive")
                        .build()
        );

        data.add(news2);

        // --- 뉴스 3 ---
        News news3 = News.builder()
                .id(3L)
                .title("카카오, 플랫폼 구조 혁신 발표")
                .description("카카오가 플랫폼 구조 개편을 발표했다.")
                .content("AI 중심으로 개편하며 플랫폼을 강화한다...")
                .publishedAt("2025-11-27")
                .author("정기자")
                .contentUrl("https://news.example.com/3")
                .thumbnailUrl("https://img.example.com/thumb3.jpg")
                .publisher("연합뉴스")
                .companyId("035720")
                .build();

        news3.getImages().add(
                NewsImage.builder().news(news3).imageUrl("https://img.example.com/kakao1.jpg").build()
        );

        news3.getCompanySentiments().add(
                CompanySentiment.builder()
                        .news(news3)
                        .company(kakao)
                        .sentiment("slightlyNegative")
                        .build()
        );

        data.add(news3);
    }

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

    @Override
    public List<News> findByCompanyId(String companyId) {
        return data.stream()
                .filter(n -> n.getCompanyId() != null && n.getCompanyId().equals(companyId))
                .sorted((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()))
                .toList();
    }



}
