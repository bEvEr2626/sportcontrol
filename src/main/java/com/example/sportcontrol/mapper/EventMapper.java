package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto toDto(SportEvent entity);

    @Mapping(target = "id", ignore = true)
    SportEvent toEntity(EventDto dto);
}
