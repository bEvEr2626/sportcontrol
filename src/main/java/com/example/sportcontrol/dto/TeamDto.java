package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Team ID (generated automatically)", example = "1")
    private Long id;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 2)
    @Schema(description = "Team name", example = "Spartak")
    private String name;
}
