
package com.example.sportcontrol.controller;

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
import com.example.sportcontrol.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PatchMapping("/{id}")
    public PlayerDto patch(@PathVariable Long id, @RequestBody PlayerDto dto) {
        return playerService.update(id, dto);
    }

    @GetMapping
    public Page<PlayerDto> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return playerService.getAllPlayers(pageable);
    }

    @GetMapping("/{id}")
    public PlayerDto getById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    @PostMapping
    public PlayerDto create(@RequestBody PlayerDto dto) {
        return playerService.create(dto);
    }

    @PutMapping("/{id}")
    public PlayerDto update(@PathVariable Long id, @RequestBody PlayerDto dto) {
        return playerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        playerService.delete(id);
    }
}
