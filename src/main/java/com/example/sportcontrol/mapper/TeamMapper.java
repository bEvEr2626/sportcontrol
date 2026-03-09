package com.example.sportcontrol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.entity.Team;
@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto toDto(Team entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "tournaments", ignore = true)
    @Mapping(target = "homeMatches", ignore = true)
    @Mapping(target = "awayMatches", ignore = true)
    Team toEntity(TeamDto dto);
}
