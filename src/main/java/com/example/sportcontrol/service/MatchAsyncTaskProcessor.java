package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchAsyncTaskProcessor {

    private final MatchService matchService;

    @Value("${app.async.match-demo-delay-ms:0}")
    private long demoDelayMs;

    @Async("matchTaskExecutor")
    public CompletableFuture<List<MatchDto>> processBulkCreateTask(List<MatchDto> matches) {
        try {
            applyDemoDelayIfConfigured();
            return CompletableFuture.completedFuture(matchService.bulkCreateTransactional(matches));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(new IllegalStateException("Async match task interrupted", ex));
        } catch (Exception ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }

    private void applyDemoDelayIfConfigured() throws InterruptedException {
        if (demoDelayMs > 0) {
            Thread.sleep(demoDelayMs);
        }
    }
}
