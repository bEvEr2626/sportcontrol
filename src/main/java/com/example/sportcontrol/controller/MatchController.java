package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.service.MatchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public List<MatchDto> getAll() {
        return matchService.getAll();
    }

    @GetMapping("/n-plus-one")
    public List<MatchDto> getAllNPlusOne() {
        return matchService.getAllNPlusOne();
    }

    @PostMapping
    public MatchDto create(@RequestBody final MatchDto dto) {
        return matchService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        matchService.delete(id);
    }

    @GetMapping("/{id}")
    public MatchDto getById(@PathVariable final Long id) {
        return matchService.getById(id);
    }
    
    @GetMapping("/search")
    public List<MatchDto> searchByLocation(
            @RequestParam final String location) {
        return matchService.getMatchesByLocation(location);
    }

    @PutMapping("/{id}")
    public MatchDto update(@PathVariable Long id, @RequestBody MatchDto dto) {
        return matchService.update(id, dto);
    }
}
