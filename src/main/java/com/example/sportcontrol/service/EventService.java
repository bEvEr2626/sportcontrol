package com.example.sportcontrol.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.repository.EventRepository;
import com.example.sportcontrol.entity.SportEvent;
import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.mapper.EventMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public EventDto createEvent(EventDto dto) {
        SportEvent entity = eventMapper.toEntity(dto);
        SportEvent savedEntity = eventRepository.save(entity);
        return eventMapper.toDto(savedEntity);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<EventDto> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }
}