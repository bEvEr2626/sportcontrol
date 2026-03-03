package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchDto toDto(Match entity);

    @Mapping(target = "id", ignore = true)
    Match toEntity(MatchDto dto);
}
