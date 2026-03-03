package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.service.MatchService;
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
public class MatchController {

    private final MatchService eventService;

    @GetMapping
    public List<MatchDto> getAll() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public MatchDto create(@RequestBody final MatchDto dto) {
        return eventService.createEvent(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/{id}")
    public MatchDto searchById(@PathVariable final Long id) {
        return eventService.getById(id);
    }
    

    @GetMapping("/search")
    public List<MatchDto> searchByLocation(
            @RequestParam final String location) {
        return eventService.getEventsByLocation(location);
    }
}
