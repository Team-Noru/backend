package com.example.noru.news.rds.service;

import com.example.noru.company.document.CompanyDocument;
import com.example.noru.company.rds.entity.Company;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.company.rds.repository.CompanySearchRepository;
import com.example.noru.news.document.NewsDocument;
import com.example.noru.news.rds.dto.S3NewsDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.repository.NewsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsSyncService {

    private final S3Client s3Client;
    private final NewsService newsService;
    private final ObjectMapper objectMapper;
    private final NewsRepository newsRepository;
    private final com.example.noru.news.repository.NewsSearchRepository newsSearchRepository;
    private final CompanyRepository companyRepository;
    private final CompanySearchRepository companySearchRepository;

    private static final String BUCKET_NAME = "noru-data";

    @Transactional(readOnly = true)
    public int fullSyncToEs() {
        log.info("MySQL -> ES 전체 동기화 시작...");

        List<News> allNews = newsRepository.findAll();
        if (allNews.isEmpty()) return 0;

        Map<Long, String> companyNameMap = companyRepository.findAll().stream()
                .collect(Collectors.toMap(Company::getId, Company::getName));

        List<NewsDocument> esDocuments = allNews.stream()
                .map(news -> {
                    // 뉴스에 있는 companyId로 Map에서 이름 꺼내오기 (없으면 "기타")
                    String companyName = companyNameMap.getOrDefault(news.getCompanyId(), "기타");

                    return NewsDocument.builder()
                            .id(news.getId())
                            .title(news.getTitle())
                            .content(news.getContent())
                            .publisher(news.getPublisher())
                            .publishedAt(news.getPublishedAt())
                            .companyCode(String.valueOf(news.getCompanyId())) // 필요하다면 stockCode로 변경 가능
                            .companyName(companyName)
                            .thumbnailUrl(news.getThumbnailUrl())
                            .build();
                })
                .collect(Collectors.toList());

        // 4. 저장
        newsSearchRepository.saveAll(esDocuments);
        log.info("전체 동기화 완료: {}건", esDocuments.size());

        return esDocuments.size();
    }

    @Transactional(readOnly = true)
    public int fullSyncCompaniesToEs() {
        log.info("MySQL -> ES 기업 정보 동기화 시작...");

        List<Company> allCompanies = companyRepository.findAll();

        List<CompanyDocument> docs = allCompanies.stream()
                .map(c -> CompanyDocument.builder()
                        .id(c.getId())
                        .companyId(c.getStockCode())
                        .name(c.getName())
                        .isDomestic(c.isDomestic())
                        .isListed(c.isListed())
                        .build())
                .collect(Collectors.toList());

        companySearchRepository.saveAll(docs);
        log.info("기업 정보 {}건 동기화 완료", docs.size());
        return docs.size();
    }

    public void syncNewsFromS3() {
        log.info("S3 뉴스 동기화 시작...");

        try {
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                    .bucket(BUCKET_NAME)
                    .build();

            ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

            for (S3Object s3Object : listRes.contents()) {
                String key = s3Object.key();

                if (!key.endsWith(".json")) continue;

                log.info("파일 처리 중: {}", key);
                processFile(key);
            }
        } catch (Exception e) {
            log.error("S3 동기화 중 오류 발생", e);
        }
    }

    private void processFile(String key) {
        try {
            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getReq);

            List<S3NewsDto> dtoList = objectMapper.readValue(s3Object, new TypeReference<List<S3NewsDto>>() {
            });

            int count = 0;
            for (S3NewsDto dto : dtoList) {
                News news = dto.toEntity();
                newsService.saveNews(news);
                count++;
            }
            log.info("파일 '{}'에서 {}건의 뉴스 저장 완료", key, count);

            deleteFile(key);
        } catch (IOException e) {
            log.error("파일 처리 실패: {}", key, e);
        }
    }

    private void deleteFile(String key) {
        DeleteObjectRequest deleteReq = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.deleteObject(deleteReq);
        log.info("S3 파일 삭제 완료: {}", key);
    }
}
