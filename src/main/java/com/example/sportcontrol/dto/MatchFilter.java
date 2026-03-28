package com.example.sportcontrol.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchFilter {
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String location;
    private Long tournamentId;
    private String homeTeamName;
    private String awayTeamName;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}