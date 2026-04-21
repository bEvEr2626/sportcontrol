package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import com.example.sportcontrol.dto.MatchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchAsyncTaskService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_FAILED = "FAILED";

    private final MatchAsyncTaskProcessor matchAsyncTaskProcessor;

    private final Map<String, TaskState> tasks = new ConcurrentHashMap<>();

    public AsyncTaskAcceptedResponseDto startBulkCreateTask(List<MatchDto> matches) {
        List<MatchDto> safeMatches = Optional.ofNullable(matches)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Matches list cannot be empty"));

        String taskId = UUID.randomUUID().toString();
        TaskState taskState = new TaskState();
        tasks.put(taskId, taskState);

        try {
            CompletableFuture<List<MatchDto>> taskFuture = matchAsyncTaskProcessor.processBulkCreateTask(List.copyOf(safeMatches));
            taskState.markRunning();
            taskFuture.whenComplete((result, error) -> {
                if (error != null) {
                    taskState.markFailed(extractErrorMessage(error));
                    return;
                }
                taskState.markCompleted(result.size());
            });
        } catch (Exception ex) {
            taskState.markFailed(extractErrorMessage(ex));
        }

        return new AsyncTaskAcceptedResponseDto(taskId);
    }

    public AsyncTaskStatusDto getTaskStatus(String taskId) {
        TaskState taskState = Optional.ofNullable(tasks.get(taskId))
            .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
        return taskState.toStatusDto(taskId);
    }

    private String extractErrorMessage(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        return Optional.ofNullable(rootCause.getMessage())
            .filter(message -> !message.isBlank())
            .orElse(rootCause.getClass().getSimpleName());
    }

    private static final class TaskState {

        private final LocalDateTime createdAt = LocalDateTime.now();

        private String status = STATUS_PENDING;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private Integer processedItems;
        private String errorMessage;

        private synchronized void markRunning() {
            status = STATUS_RUNNING;
            startedAt = LocalDateTime.now();
            errorMessage = null;
        }

        private synchronized void markCompleted(Integer processedCount) {
            status = STATUS_COMPLETED;
            completedAt = LocalDateTime.now();
            processedItems = processedCount;
            errorMessage = null;
        }

        private synchronized void markFailed(String message) {
            status = STATUS_FAILED;
            completedAt = LocalDateTime.now();
            errorMessage = message;
        }

        private synchronized AsyncTaskStatusDto toStatusDto(String taskId) {
            return new AsyncTaskStatusDto(
                taskId,
                status,
                createdAt,
                startedAt,
                completedAt,
                processedItems,
                errorMessage
            );
        }
    }
}