package com.example.noru.admin.controller;

import com.example.noru.news.rds.dto.NewsEsDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.entity.OutboxEvent;
import com.example.noru.news.rds.repository.NewsRepository;
import com.example.noru.news.rds.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final NewsRepository newsRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/backfill/outbox")
    public String backfillOutbox() {
        List<News> allNews = newsRepository.findAll();
        int count = 0;

        for (News news : allNews) {
            try {
                // DTO 변환
                NewsEsDto dto = NewsEsDto.from(news);
                String payload = objectMapper.writeValueAsString(dto);

                // Outbox 이벤트 강제 생성
                OutboxEvent event = OutboxEvent.builder()
                        .aggregateType("NEWS")
                        .aggregateId(news.getId())
                        .eventType("BACKFILL")
                        .payload(payload)
                        .status("PENDING") // PENDING으로 넣어야 스케줄러가 가져감
                        .createdAt(LocalDateTime.now())
                        .build();

                outboxEventRepository.save(event);
                count++;
            } catch (Exception e) {
                log.error("Failed backfill for news id: {}", news.getId(), e);
            }
        }
        return count + "건의 뉴스에 대해 Outbox 이벤트를 생성했습니다.";
    }
}