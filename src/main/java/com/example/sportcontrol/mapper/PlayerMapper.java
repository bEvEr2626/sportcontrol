package com.example.sportcontrol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.entity.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    
    @Mapping(source = "team.id", target = "teamId")
    PlayerDto toDto(Player entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    Player toEntity(PlayerDto dto);
}
