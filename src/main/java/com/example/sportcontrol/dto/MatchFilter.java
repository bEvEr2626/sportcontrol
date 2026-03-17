package com.example.sportcontrol.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchFilter {
    private String name;
    private String location;
    private Long tournamentId;
    private String homeTeamName;
    private String awayTeamName;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}