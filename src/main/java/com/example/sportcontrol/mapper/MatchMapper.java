package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    @Mapping(source = "homeTeam.id", target = "homeTeamId")
    @Mapping(source = "homeTeam.name", target = "homeTeamName")
    @Mapping(source = "awayTeam.id", target = "awayTeamId")
    @Mapping(source = "awayTeam.name", target = "awayTeamName")
    MatchDto toDto(Match entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tournament", ignore = true)
    @Mapping(target = "homeTeam", ignore = true)
    @Mapping(target = "awayTeam", ignore = true)
    Match toEntity(MatchDto dto);
}
