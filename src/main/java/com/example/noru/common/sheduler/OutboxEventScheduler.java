package com.example.noru.common.scheduler;

import com.example.noru.news.rds.entity.OutboxEvent;
import com.example.noru.news.rds.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    @Transactional
    public void processOutboxEvents() {
        // PENDING 상태인 이벤트 조회
        List<OutboxEvent> events = outboxEventRepository.findAllByStatus("PENDING");

        if (events.isEmpty()) return;

        for (OutboxEvent event : events) {
            try {
                // aggregateType에 따라 인덱스 이름 결정
                // NEWS -> news_index
                // COMPANY -> company_index (자동으로 소문자 변환되어 매핑됨)
                String indexName = event.getAggregateType().toLowerCase() + "_index";

                // ES 쿼리 생성
                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(event.getAggregateId().toString())
                        .withSource(event.getPayload())
                        .build();

                // ES로 전송
                elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));

                // 성공 시 상태 변경
                event.changeStatus("PUBLISHED");

            } catch (Exception e) {
                log.error("이벤트 전송 실패 ID: {}", event.getId(), e);
            }
        }
    }
}