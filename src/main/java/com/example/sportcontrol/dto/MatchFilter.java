package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Match name to search for", example = "Cup Final")
    private String name;

    @Size(max = 255)
    @Schema(description = "Location to search for", example = "Luzhniki Stadium")
    private String location;

    @Schema(description = "Tournament ID to filter", type = "integer", format = "int64", example = "2")
    private Long tournamentId;

    @Schema(description = "Home team name to filter", example = "Spartak")
    private String homeTeamName;

    @Schema(description = "Away team name to filter", example = "Zenit")
    private String awayTeamName;

    @Schema(description = "Start date (from)", example = "2026-05-01T00:00:00")
    private LocalDateTime dateFrom;

    @Schema(description = "End date (to)", example = "2026-05-31T23:59:59")
    private LocalDateTime dateTo;
}