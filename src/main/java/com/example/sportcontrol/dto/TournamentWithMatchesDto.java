package com.example.sportcontrol.dto;

import java.util.List;
import lombok.Data;

@Data
public class TournamentWithMatchesDto {

    private String tournamentName;
    private String tournamentSlug;
    private Long sportId;
    private List<MatchDto> matches;
}
