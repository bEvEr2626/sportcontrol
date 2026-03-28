package com.example.sportcontrol.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SportDto {
    private Long id;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 2)
    private String name;
}
