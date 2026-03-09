package com.example.sportcontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.example.sportcontrol.dto.TournamentDto;
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
public class TournamentController {
    private final TournamentService tournamentService;

    @GetMapping
    public List<TournamentDto> getAll() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/n-plus-one")
    public List<TournamentDto> getAllNPlusOne() {
        return tournamentService.getAllTournamentsNPlusOne();
    }

    @PostMapping
    public TournamentDto create(@RequestBody TournamentDto dto) {
        return tournamentService.create(dto);
    }
    
    @GetMapping("/{id}")
    public TournamentDto getById(@PathVariable Long id) {
        return tournamentService.getById(id);
    }

    @PutMapping("/{id}")
    public TournamentDto update(@PathVariable Long id, @RequestBody TournamentDto dto) {
        return tournamentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tournamentService.delete(id);
    }
}
