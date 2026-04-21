package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.MatchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchAsyncTaskProcessorTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchAsyncTaskProcessor processor;

    @Test
    void processBulkCreateTaskReturnsCompletedFutureWhenServiceSucceeds() {
        List<MatchDto> input = List.of(buildMatchDto(1L));
        List<MatchDto> created = List.of(buildMatchDto(101L));
        when(matchService.bulkCreateTransactional(input)).thenReturn(created);

        CompletableFuture<List<MatchDto>> future = processor.processBulkCreateTask(input);

        assertEquals(created, future.join());
        verify(matchService).bulkCreateTransactional(input);
    }

    @Test
    void processBulkCreateTaskReturnsFailedFutureWhenServiceThrows() {
        List<MatchDto> input = List.of(buildMatchDto(1L));
        when(matchService.bulkCreateTransactional(input)).thenThrow(new IllegalArgumentException("Invalid matches"));

        CompletableFuture<List<MatchDto>> future = processor.processBulkCreateTask(input);

        CompletionException exception = assertThrows(CompletionException.class, future::join);
        assertEquals("Invalid matches", exception.getCause().getMessage());
        verify(matchService).bulkCreateTransactional(input);
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