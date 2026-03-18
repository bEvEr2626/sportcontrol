package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.TournamentWithMatchesDto;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.service.TournamentBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments/batch")
@RequiredArgsConstructor
public class TournamentBatchController {

    private final TournamentBatchService batchService;

    @PostMapping
    public String createTournamentWithMatches(@Valid @RequestBody TournamentWithMatchesDto dto) {
        try {
            Tournament tournament = batchService.createTournamentWithMatches(dto);
            return "OK: tournament id=" + tournament.getId() + " and all matches saved";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
}