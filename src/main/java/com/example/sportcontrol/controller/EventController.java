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

/**
 * Controller for event operations.
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    /** The event service. */
    private final EventService eventService;

    /**
     * Gets all events.
     * @return list of events.
     */
    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAllEvents();
    }

    /**
     * Creates a new event.
     * @param dto event data.
     * @return created event.
     */
    @PostMapping
    public EventDto create(@RequestBody final EventDto dto) {
        return eventService.createEvent(dto);
    }

    /**
     * Deletes an event.
     * @param id event id.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        eventService.deleteEvent(id);
    }

    /**
     * Searches events by location.
     * @param location event location.
     * @return list of found events.
     */
    @GetMapping("/search")
    public List<EventDto> searchByLocation(
            @RequestParam final String location) {
        return eventService.getEventsByLocation(location);
    }
}
