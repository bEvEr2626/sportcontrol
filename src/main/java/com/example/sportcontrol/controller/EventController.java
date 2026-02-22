package com.example.sportcontrol.controller;

import java.util.*;
import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public EventDto create(@RequestBody EventDto dto) {
        return eventService.createEvent(dto);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/search")
    public List<EventDto> searchByLocation(@RequestParam String location) {
        return eventService.getEventsByLocation(location);
    }
}