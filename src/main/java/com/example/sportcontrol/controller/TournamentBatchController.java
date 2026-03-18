package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.TournamentWithMatchesDto;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.service.TournamentBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments/batch")
@RequiredArgsConstructor
public class TournamentBatchController {

    private final TournamentBatchService batchService;

    @PostMapping
    public ResponseEntity<String> createTournamentWithMatches(@Valid @RequestBody TournamentWithMatchesDto dto) {
        try {
            batchService.createTournamentWithMatches(dto);

            return ResponseEntity.ok("Tournament created successfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Referenced entity not found");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}