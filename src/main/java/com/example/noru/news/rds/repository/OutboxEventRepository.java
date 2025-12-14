package com.example.noru.news.rds.repository;

import com.example.noru.news.rds.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    // 아직 처리되지 않은(PENDING) 이벤트만 조회하는 메서드 (나중에 스케줄러가 씀)
    List<OutboxEvent> findAllByStatus(String status);
}