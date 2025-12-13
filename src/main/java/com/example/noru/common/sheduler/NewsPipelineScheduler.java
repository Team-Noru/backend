package com.example.noru.common.sheduler;

import com.example.noru.news.rds.service.NewsSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsPipelineScheduler {

    private final NewsSyncService newsSyncService;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void runPipeline() {
        newsSyncService.syncNewsFromS3();
    }
}
