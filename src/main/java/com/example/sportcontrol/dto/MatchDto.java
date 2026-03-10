package com.example.sportcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String location;

    @NotNull
    private LocalDateTime date;

    private Long tournamentId;

    private String tournamentName;

    @NotNull
    private Long homeTeamId;

    private String homeTeamName;

    @NotNull
    private Long awayTeamId;

    private String awayTeamName;
}
