
package com.example.sportcontrol.controller;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import com.example.sportcontrol.dto.MatchDto;
import jakarta.validation.Valid;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PatchMapping("/{id:\\d+}")
    public MatchDto patch(@PathVariable Long id, @RequestBody @Valid MatchDto dto) {
        return matchService.update(id, dto);
    }

    @GetMapping
    public Page<MatchDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return matchService.findMatches(new MatchFilter(), page, size);
    }

    @GetMapping("/{id:\\d+}")
    public MatchDto getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    @PostMapping
    public MatchDto create(@RequestBody @Valid MatchDto dto) {
        return matchService.create(dto);
    }

    @PutMapping("/{id:\\d+}")
    public MatchDto update(@PathVariable Long id, @RequestBody @Valid MatchDto dto) {
        return matchService.update(id, dto);
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable Long id) {
        matchService.delete(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<MatchDto>> search(
        @RequestBody @Valid MatchFilter filter,
        @RequestParam(defaultValue = "jpql") String queryType,
        @Parameter(
            description = "Page number (0-based)",
            example = "0",
            in = ParameterIn.QUERY,
            name = "page"
        )
        @RequestParam(defaultValue = "0") int page,
        @Parameter(
            description = "Page size",
            example = "10",
            in = ParameterIn.QUERY,
            name = "size"
        )
        @RequestParam(defaultValue = "10") int size
    ) {
        boolean useNative = "native".equalsIgnoreCase(queryType);

        Page<MatchDto> result = useNative
            ? matchService.findMatchesNative(filter, page, size)
            : matchService.findMatches(filter, page, size);

        return ResponseEntity.ok(result);
    }
}