package com.example.sportcontrol.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Match ID (generated automatically)", example = "1")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Match name", example = "Cup Final")
    private String name;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Location", example = "Luzhniki Stadium")
    private String location;

    @NotNull
    @Schema(description = "Match date and time (format: yyyy-MM-dd'T'HH:mm:ss)", example = "2026-05-01T18:00:00")
    private LocalDateTime date;

    @Schema(description = "Tournament ID", example = "2")
    private Long tournamentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Tournament name", example = "Russian Cup")
    private String tournamentName;

    @NotNull
    @Schema(description = "Home team ID", example = "10")
    private Long homeTeamId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Home team name", example = "Spartak")
    private String homeTeamName;

    @NotNull
    @Schema(description = "Away team ID", example = "11")
    private Long awayTeamId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Away team name", example = "Zenit")
    private String awayTeamName;
}
