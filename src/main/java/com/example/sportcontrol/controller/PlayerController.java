package com.example.sportcontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public List<PlayerDto> getAll() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/n-plus-one")
    public List<PlayerDto> getAllNPlusOne() {
        return playerService.getAllPlayersNPlusOne();
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
