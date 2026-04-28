package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AsyncLoadDemoServiceTest {

    @Mock
    private AsyncLoadDemoProcessor asyncLoadDemoProcessor;

    @InjectMocks
    private AsyncLoadDemoService service;

    @Test
    void startTaskReturnsTaskIdAndMarksTaskRunning() {
        CompletableFuture<Void> taskFuture = new CompletableFuture<>();
        when(asyncLoadDemoProcessor.runTask(5000L)).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startTask(5000L);
        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertNotNull(response.getTaskId());
        assertEquals("RUNNING", status.getStatus());
        assertNotNull(status.getStartedAt());
        assertNull(status.getCompletedAt());
    }

    @Test
    void startTaskMarksTaskCompletedWhenFutureFinishesSuccessfully() {
        CompletableFuture<Void> taskFuture = new CompletableFuture<>();
        when(asyncLoadDemoProcessor.runTask(5000L)).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startTask(5000L);
        taskFuture.complete(null);

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("COMPLETED", status.getStatus());
        assertEquals(1, status.getProcessedItems());
        assertNull(status.getErrorMessage());
    }

    @Test
    void startTaskMarksTaskFailedWhenFutureCompletesExceptionally() {
        CompletableFuture<Void> taskFuture = new CompletableFuture<>();
        when(asyncLoadDemoProcessor.runTask(5000L)).thenReturn(taskFuture);

        AsyncTaskAcceptedResponseDto response = service.startTask(5000L);
        taskFuture.completeExceptionally(new IllegalStateException("demo failed"));

        AsyncTaskStatusDto status = service.getTaskStatus(response.getTaskId());

        assertEquals("FAILED", status.getStatus());
        assertEquals("demo failed", status.getErrorMessage());
    }

    @Test
    void startTaskRethrowsWhenExecutorRejectsTask() {
        when(asyncLoadDemoProcessor.runTask(5000L))
            .thenThrow(new RejectedExecutionException("queue full"));

        RejectedExecutionException exception = assertThrows(
            RejectedExecutionException.class,
            () -> service.startTask(5000L)
        );

        assertEquals("queue full", exception.getMessage());
    }

    @Test
    void startTaskThrowsWhenDurationIsInvalid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.startTask(0)
        );

        assertEquals("durationMs must be > 0", exception.getMessage());
    }

    @Test
    void getTaskStatusThrowsWhenTaskDoesNotExist() {
        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.getTaskStatus("missing-task")
        );

        assertEquals("Task not found: missing-task", exception.getMessage());
    }
}
