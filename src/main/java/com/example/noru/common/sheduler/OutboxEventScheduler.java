package com.example.noru.common.sheduler;

import com.example.noru.news.rds.dto.NewsEsDto;
import com.example.noru.news.rds.entity.News;
import com.example.noru.news.rds.entity.OutboxEvent;
import com.example.noru.news.rds.repository.OutboxEventRepository;
import com.example.noru.news.rds.service.NewsSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final NewsSearchService newsSearchService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void processOutboxEvents() {
        // 1. PENDING 이벤트 조회
        List<OutboxEvent> events = outboxEventRepository.findAllByStatus("PENDING");

        if (events.isEmpty()) return;

        for (OutboxEvent event : events) {
            try {
                if ("NEWS".equals(event.getAggregateType())) {
                    // [수정됨] JSON -> NewsEsDto 변환 (NewsEntity가 아님!)
                    NewsEsDto dto = objectMapper.readValue(event.getPayload(), NewsEsDto.class);

                    // Service로 전달
                    newsSearchService.saveToElasticsearch(dto);
                }

                // 성공 처리
                event.changeStatus("PUBLISHED");

            } catch (Exception e) {
                log.error("이벤트 처리 실패 ID: {}", event.getId(), e);
            }
        }
    }
}