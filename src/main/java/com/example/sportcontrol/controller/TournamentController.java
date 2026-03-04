package com.example.sportcontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.service.TournamentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public TournamentDto create(@RequestBody TournamentDto dto) {
        return tournamentService.createTournament(dto);
    }
    
}
