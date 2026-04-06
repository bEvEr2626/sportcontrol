
package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.example.sportcontrol.dto.TournamentDto;
import jakarta.validation.Valid;
import com.example.sportcontrol.service.TournamentService;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Operations for managing tournaments")
public class TournamentController {
    private final TournamentService tournamentService;

    @PatchMapping("/{id}")
    @Operation(summary = "Patch tournament", description = "Partially updates an existing tournament by ID")
    public TournamentDto patch(@PathVariable Long id, @RequestBody @Valid TournamentDto dto) {
        return tournamentService.update(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get all tournaments", description = "Returns a list of all tournaments")
    public List<TournamentDto> getAll() {
        return tournamentService.getAllTournaments();
    }


    @PostMapping
    @Operation(summary = "Create tournament", description = "Creates a new tournament")
    public TournamentDto create(@RequestBody @Valid TournamentDto dto) {
        return tournamentService.create(dto);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get tournament by ID", description = "Returns a tournament by ID")
    public TournamentDto getById(@PathVariable Long id) {
        return tournamentService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tournament", description = "Fully updates an existing tournament by ID")
    public TournamentDto update(@PathVariable Long id, @RequestBody @Valid TournamentDto dto) {
        return tournamentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tournament", description = "Deletes a tournament by ID")
    public void delete(@PathVariable Long id) {
        tournamentService.delete(id);
    }
}
