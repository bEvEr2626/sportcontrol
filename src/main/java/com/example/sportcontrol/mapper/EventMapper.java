package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDto toDto(SportEvent entity) {
        if (entity == null) return null;

        EventDto dto = new EventDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setDate(entity.getDate());
        return dto;
    }

    public SportEvent toEntity(EventDto dto) {
        if (dto == null) return null;

        SportEvent entity = new SportEvent();
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setDate(dto.getDate());
        return entity;
    }
}