package com.example.noru.news.rds.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // 예: "NEWS"

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId; // 예: newsId

    @Column(name = "event_type", nullable = false)
    private String eventType; // 예: "CREATED"

    // MySQL의 JSON 타입은 JPA에서 String으로 매핑하고, columnDefinition을 명시하면 됩니다.
    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private String payload;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    private String status; // "PENDING", "PUBLISHED"

    public void changeStatus(String status) {
        this.status = status;
    }
}