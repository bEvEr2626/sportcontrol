
package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import jakarta.validation.Valid;
import com.example.sportcontrol.service.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Operations for managing teams")
public class TeamController {

    private final TeamService teamService;

    @PatchMapping("/{id}")
    @Operation(summary = "Patch team", description = "Partially updates an existing team by ID")
    public TeamDto patch(@PathVariable Long id, @RequestBody @Valid TeamDto dto) {
        return teamService.update(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get all teams", description = "Returns a list of all teams")
    public List<TeamDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID", description = "Returns a team by ID")
    public TeamDto getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create team", description = "Creates a new team")
    public TeamDto create(@RequestBody @Valid TeamDto dto) {
        return teamService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update team", description = "Fully updates an existing team by ID")
    public TeamDto update(@PathVariable Long id, @RequestBody @Valid TeamDto dto) {
        return teamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team", description = "Deletes a team by ID")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
