package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskAcceptedResponseDto {

    @Schema(description = "Async task identifier", example = "3e3f95e2-c530-4bcf-a534-17be32789f56")
    private String taskId;
}