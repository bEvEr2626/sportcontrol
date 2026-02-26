package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.entity.SportEvent;
import com.example.sportcontrol.mapper.EventMapper;
import com.example.sportcontrol.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    public final List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public final EventDto createEvent(final EventDto dto) {
        SportEvent entity = eventMapper.toEntity(dto);
        SportEvent savedEntity = eventRepository.save(entity);
        return eventMapper.toDto(savedEntity);
    }

    public final void deleteEvent(final Long id) {
        eventRepository.deleteById(id);
    }

    public final EventDto getById(final Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDto)
                .orElse(null);
    }

    public final List<EventDto> getEventsByLocation(final String location) {
        return eventRepository.findByLocation(location)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }
}
