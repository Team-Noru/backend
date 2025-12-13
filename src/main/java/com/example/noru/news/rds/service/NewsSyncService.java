package com.example.noru.news.rds.service;

import com.example.noru.news.rds.dto.S3NewsDto;
import com.example.noru.news.rds.entity.News;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsSyncService {

    private final S3Client s3Client;
    private final NewsService newsService;
    private final ObjectMapper objectMapper;

    private static final String BUCKET_NAME = "noru-data";

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
