package com.example.sportcontrol.controller;

import java.util.*;
import com.example.sportcontrol.dto.EventDto;
import com.example.sportcontrol.service.EventService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAllEvents();
    }
    
}