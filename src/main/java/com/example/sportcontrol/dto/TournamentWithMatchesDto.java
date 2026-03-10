package com.example.sportcontrol.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class TournamentWithMatchesDto {

    @NotBlank
    @Size(max = 255)
    private String tournamentName;

    @NotBlank
    @Size(max = 255)
    private String tournamentSlug;

    @NotNull
    private Long sportId;

    @NotEmpty
    @Valid
    private List<MatchDto> matches;
}
