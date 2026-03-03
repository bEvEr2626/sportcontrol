package com.example.sportcontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.service.SportService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/sports")
@RequiredArgsConstructor
public class SportController {
    private final SportService sportService;

    @GetMapping
    public List<SportDto> getAll() {
        return sportService.getAllSports();
    }
    
    @PostMapping
    public SportDto create(@RequestBody SportDto dto) {
        return sportService.createSport(dto);
    }
}
