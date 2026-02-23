package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Event entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Converts entity to DTO.
     * @param entity the sport event entity.
     * @return the event DTO.
     */
    EventDto toDto(SportEvent entity);

    /**
     * Converts DTO to entity, ignoring the ID field.
     * @param dto the event DTO.
     * @return the sport event entity.
     */
    @Mapping(target = "id", ignore = true)
    SportEvent toEntity(EventDto dto);
}
