package com.example.noru.news.controller;

import com.example.noru.news.rds.service.NewsSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news/sync")
@RequiredArgsConstructor
public class NewsSyncController {

    private final NewsSyncService newsSyncService;

    @PostMapping("/full")
    public ResponseEntity<String> fullSync() {
        int count = newsSyncService.fullSyncToEs();
        return ResponseEntity.ok("전체 동기화 완료! 총 " + count + "건 처리됨.");
    }

    @PostMapping("/companies/full")
    public ResponseEntity<String> syncCompanies() {
        int count = newsSyncService.fullSyncCompaniesToEs();
        return ResponseEntity.ok("기업 동기화 완료: " + count + "건");
    }
}
