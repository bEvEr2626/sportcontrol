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
public ResponseEntity<ApiResponse<Long>> createTournamentWithMatches(
        @Valid @RequestBody TournamentWithMatchesDto dto) {
    try {
        Tournament tournament = batchService.createTournamentWithMatches(dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, tournament.getId(), "Tournament and matches saved")
        );
    } catch (EntityNotFoundException ex) {
        LOG.warn("Entity not found during tournament creation", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, null, "Referenced entity not found"));
    } catch (Exception ex) {
        LOG.error("Unexpected error during tournament creation", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, "Unexpected error occurred"));
    }
}
}