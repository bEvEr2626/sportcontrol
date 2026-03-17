package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public Page<MatchDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return matchService.findMatches(new MatchFilter(), page, size);
    }

    @GetMapping("/{id}")
    public MatchDto getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    @PostMapping
    public MatchDto create(@RequestBody MatchDto dto) {
        return matchService.create(dto);
    }

    @PutMapping("/{id}")
    public MatchDto update(@PathVariable Long id, @RequestBody MatchDto dto) {
        return matchService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        matchService.delete(id);
    }

    @GetMapping("/search")
    public Page<MatchDto> search(
            @ModelAttribute MatchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return matchService.findMatches(filter, page, size);
    }

    @GetMapping("/search/native")
    public Page<MatchDto> searchNative(
            @ModelAttribute MatchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return matchService.findMatchesNative(filter, page, size);
    }
}