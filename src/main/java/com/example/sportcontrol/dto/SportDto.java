package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SportDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Sport ID (generated automatically)", example = "1")
    private Long id;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 2)
    @Schema(description = "Sport name", example = "Football")
    private String name;
}
