package com.example.sportcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PlayerDto {
    private Long id;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 2)
    private String name;

    @NotNull
    private Long teamId;
}
