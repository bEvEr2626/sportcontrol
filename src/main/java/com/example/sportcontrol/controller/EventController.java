package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.service.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public EventDto create(@RequestBody final EventDto dto) {
        return eventService.createEvent(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/search")
    public List<EventDto> searchByLocation(
            @RequestParam final String location) {
        return eventService.getEventsByLocation(location);
    }
}
