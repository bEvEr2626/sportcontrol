package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.entity.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportMapper {

    SportDto toDto(Sport entity);

    @Mapping(target = "id", ignore = true)
    Sport toEntity(SportDto dto);
}