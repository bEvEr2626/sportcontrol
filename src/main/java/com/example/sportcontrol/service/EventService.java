package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import com.example.sportcontrol.mapper.EventMapper;
import com.example.sportcontrol.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing sport events.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    /** The repository for event data. */
    private final EventRepository eventRepository;

    /** The mapper for event conversions. */
    private final EventMapper eventMapper;

    /**
     * Retrieves all events.
     * @return list of event DTOs.
     */
    public final List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .toList();
    }

    /**
     * Creates a new event.
     * @param dto the event data transfer object.
     * @return the created event DTO.
     */
    public final EventDto createEvent(final EventDto dto) {
        SportEvent entity = eventMapper.toEntity(dto);
        SportEvent savedEntity = eventRepository.save(entity);
        return eventMapper.toDto(savedEntity);
    }

    /**
     * Deletes an event by ID.
     * @param id the identifier of the event.
     */
    public final void deleteEvent(final Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Finds events by location.
     * @param location the location string.
     * @return list of found event DTOs.
     */
    public final List<EventDto> getEventsByLocation(final String location) {
        return eventRepository.findByLocation(location)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }
}
