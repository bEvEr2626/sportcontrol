
package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.sportcontrol.dto.PlayerDto;
import jakarta.validation.Valid;
import com.example.sportcontrol.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
@Tag(name = "Players", description = "Operations for managing players")
public class PlayerController {

    private final PlayerService playerService;

    @PatchMapping("/{id}")
    @Operation(summary = "Patch player", description = "Partially updates an existing player by ID")
    public PlayerDto patch(@PathVariable Long id, @RequestBody @Valid PlayerDto dto) {
        return playerService.update(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get players", description = "Returns a paginated list of players")
    public Page<PlayerDto> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return playerService.getAllPlayers(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID", description = "Returns a player by ID")
    public PlayerDto getById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create player", description = "Creates a new player")
    public PlayerDto create(@RequestBody @Valid PlayerDto dto) {
        return playerService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update player", description = "Fully updates an existing player by ID")
    public PlayerDto update(@PathVariable Long id, @RequestBody @Valid PlayerDto dto) {
        return playerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete player", description = "Deletes a player by ID")
    public void delete(@PathVariable Long id) {
        playerService.delete(id);
    }
}
