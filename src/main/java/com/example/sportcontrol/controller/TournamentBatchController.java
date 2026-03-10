package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.TournamentWithMatchesDto;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.service.TournamentBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments/batch")
@RequiredArgsConstructor
public class TournamentBatchController {

    private final TournamentBatchService batchService;

    @PostMapping("/without-tx")
    public String withoutTransactional(
            @Valid @RequestBody TournamentWithMatchesDto dto) {
        try {
            batchService.createTournamentWithMatches(dto);
            return "OK: tournament and all matches saved";
        } catch (EntityNotFoundException ex) {
            return "ERROR (partial save): " + ex.getMessage();
        }
    }

    @PostMapping("/with-tx")
    public String withTransactional(
            @Valid @RequestBody TournamentWithMatchesDto dto) {
        try {
            batchService.createTournamentWithMatchesTransactional(dto);
            return "OK: tournament and all matches saved";
        } catch (EntityNotFoundException ex) {
            return "ERROR (rolled back): " + ex.getMessage();
        }
    }
}
