
package com.example.sportcontrol.controller;

import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.service.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PatchMapping("/{id}")
    public TeamDto patch(@PathVariable Long id, @RequestBody TeamDto dto) {
        return teamService.update(id, dto);
    }

    @GetMapping
    public List<TeamDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    public TeamDto getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @PostMapping
    public TeamDto create(@RequestBody TeamDto dto) {
        return teamService.create(dto);
    }

    @PutMapping("/{id}")
    public TeamDto update(@PathVariable Long id, @RequestBody TeamDto dto) {
        return teamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
