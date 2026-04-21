package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskStatusDto {

    @Schema(description = "Async task identifier", example = "3e3f95e2-c530-4bcf-a534-17be32789f56")
    private String taskId;

    @Schema(description = "Current status", example = "RUNNING")
    private String status;

    @Schema(description = "Task creation time", example = "2026-04-21T15:20:00")
    private LocalDateTime createdAt;

    @Schema(description = "Task start time", example = "2026-04-21T15:20:01")
    private LocalDateTime startedAt;

    @Schema(description = "Task completion time", example = "2026-04-21T15:20:05")
    private LocalDateTime completedAt;

    @Schema(description = "Number of processed items for completed task", example = "3")
    private Integer processedItems;

    @Schema(description = "Error details for failed task", example = "Matches list cannot be empty")
    private String errorMessage;
}