package com.example.sportcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class TournamentTeamsDto {

    @NotEmpty
    @Schema(description = "Team IDs to add to the tournament", example = "[1,2,3]")
    private List<Long> teamIds;
}
