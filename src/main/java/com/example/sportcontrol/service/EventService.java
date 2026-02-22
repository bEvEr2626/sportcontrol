package com.example.sportcontrol.service;

import java.util.*;

import org.springframework.stereotype.Service;
import com.example.sportcontrol.repository.EventRepository;
import com.example.sportcontrol.entity.SportEvent;
import com.example.sportcontrol.dto.EventDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    public List<EventDto> getAllEvents(){
        List<SportEvent> events = eventRepository.findAll();
        List<EventDto> result = new ArrayList<>();
        for(SportEvent event : events){
            EventDto dto = new EventDto();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setLocation(event.getLocation());
            dto.setDate(event.getDate());

            result.add(dto);
        }
        return result;
    }
}
