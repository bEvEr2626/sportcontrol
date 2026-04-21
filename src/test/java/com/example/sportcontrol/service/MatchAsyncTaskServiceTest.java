package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import com.example.sportcontrol.dto.MatchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchAsyncTaskServiceTest {

    @Mock
    private MatchAsyncTaskProcessor matchAsyncTaskProcessor;

    @InjectMocks
    private MatchAsyncTaskService service;

    @Test
    void startBulkCreateTaskReturnsTaskIdAndMarksTaskRunning() {
        MatchDto match = buildMatchDto(1L);
        CompletableFuture<List<MatchDto>> taskFuture = new CompletableFuture<>();
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList())).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(match));
        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertNotNull(response.getTaskId());
        assertEquals("RUNNING", status.getStatus());
        assertNotNull(status.getCreatedAt());
        assertNotNull(status.getStartedAt());
        assertNull(status.getCompletedAt());
        assertNull(status.getProcessedItems());
        assertNull(status.getErrorMessage());
        verify(matchAsyncTaskProcessor).processBulkCreateTask(anyList());
    }

    @Test
    void startBulkCreateTaskMarksTaskCompletedWhenFutureFinishesSuccessfully() {
        MatchDto first = buildMatchDto(1L);
        MatchDto second = buildMatchDto(2L);
        CompletableFuture<List<MatchDto>> taskFuture = new CompletableFuture<>();
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList())).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(first, second));
        taskFuture.complete(List.of(first, second));

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("COMPLETED", status.getStatus());
        assertNotNull(status.getCompletedAt());
        assertEquals(2, status.getProcessedItems());
        assertNull(status.getErrorMessage());
    }

    @Test
    void startBulkCreateTaskMarksTaskFailedWhenFutureCompletesExceptionally() {
        MatchDto match = buildMatchDto(1L);
        CompletableFuture<List<MatchDto>> taskFuture = new CompletableFuture<>();
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList())).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(match));
        taskFuture.completeExceptionally(new IllegalStateException("Bulk create failed"));

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("FAILED", status.getStatus());
        assertNotNull(status.getCompletedAt());
        assertEquals("Bulk create failed", status.getErrorMessage());
    }

    @Test
    void startBulkCreateTaskUsesRootCauseMessageWhenNestedExceptionIsThrown() {
        MatchDto match = buildMatchDto(1L);
        CompletableFuture<List<MatchDto>> taskFuture = new CompletableFuture<>();
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList())).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(match));
        taskFuture.completeExceptionally(new RuntimeException("outer", new IllegalArgumentException("inner")));

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("FAILED", status.getStatus());
        assertEquals("inner", status.getErrorMessage());
    }

    @Test
    void startBulkCreateTaskUsesExceptionClassNameWhenMessageIsBlank() {
        MatchDto match = buildMatchDto(1L);
        CompletableFuture<List<MatchDto>> taskFuture = new CompletableFuture<>();
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList())).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(match));
        taskFuture.completeExceptionally(new IllegalStateException("   "));

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("FAILED", status.getStatus());
        assertEquals("IllegalStateException", status.getErrorMessage());
    }

    @Test
    void startBulkCreateTaskMarksTaskFailedWhenProcessorThrowsImmediately() {
        MatchDto match = buildMatchDto(1L);
        when(matchAsyncTaskProcessor.processBulkCreateTask(anyList()))
            .thenThrow(new IllegalStateException("Executor unavailable"));

        AsyncTaskAcceptedResponseDto response = service.startBulkCreateTask(List.of(match));
        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("FAILED", status.getStatus());
        assertEquals("Executor unavailable", status.getErrorMessage());
    }

    @Test
    void startBulkCreateTaskThrowsWhenListIsNull() {
        List<MatchDto> matches = null;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.startBulkCreateTask(matches)
        );

        assertEquals("Matches list cannot be empty", exception.getMessage());
    }

    @Test
    void startBulkCreateTaskThrowsWhenListIsEmpty() {
        List<MatchDto> matches = List.of();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.startBulkCreateTask(matches)
        );

        assertEquals("Matches list cannot be empty", exception.getMessage());
    }

    @Test
    void getTaskStatusThrowsWhenTaskDoesNotExist() {
        String taskId = "missing-task";
        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.getTaskStatus(taskId)
        );

        assertEquals("Task not found: missing-task", exception.getMessage());
    }

    private MatchDto buildMatchDto(Long id) {
        return new MatchDto(
            id,
            "Cup Final",
            "Stadium",
            LocalDateTime.of(2026, 5, 1, 18, 0),
            2L,
            "Premier Cup",
            3L,
            "Home Team",
            4L,
            "Away Team"
        );
    }
}