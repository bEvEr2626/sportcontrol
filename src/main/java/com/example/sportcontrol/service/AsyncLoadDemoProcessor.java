package com.example.sportcontrol.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncLoadDemoProcessor {

    @Async("matchTaskExecutor")
    public CompletableFuture<Void> runTask(long durationMs) {
        try {
            Thread.sleep(durationMs);
            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(new IllegalStateException("Async demo task interrupted", ex));
        }
    }
}
