package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Player ID (generated automatically)", example = "1")
    private Long id;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 2)
    @Schema(description = "Player name", example = "John Smith")
    private String name;

    @NotNull
    @Schema(description = "Team ID", example = "10")
    private Long teamId;
}
