package com.example.sportcontrol.mapper;

import org.mapstruct.Mapper;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Tournament;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    @Mapping(source = "sport.id", target = "sportId")
    TournamentDto toDto(Tournament entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sport", ignore = true)
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "teams", ignore = true)
    Tournament toEntity(TournamentDto dto);    
}
