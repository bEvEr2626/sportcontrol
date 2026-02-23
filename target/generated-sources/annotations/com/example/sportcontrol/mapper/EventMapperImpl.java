package com.example.sportcontrol.mapper;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-23T10:52:41+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Arch Linux)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto toDto(SportEvent entity) {
        if ( entity == null ) {
            return null;
        }

        EventDto eventDto = new EventDto();

        eventDto.setId( entity.getId() );
        eventDto.setName( entity.getName() );
        eventDto.setLocation( entity.getLocation() );
        eventDto.setDate( entity.getDate() );

        return eventDto;
    }

    @Override
    public SportEvent toEntity(EventDto dto) {
        if ( dto == null ) {
            return null;
        }

        SportEvent sportEvent = new SportEvent();

        sportEvent.setName( dto.getName() );
        sportEvent.setLocation( dto.getLocation() );
        sportEvent.setDate( dto.getDate() );

        return sportEvent;
    }
}
