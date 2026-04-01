
package com.example.sportcontrol.controller;

import org.springframework.web.bind.annotation.PatchMapping;

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
        @PageableDefault(size = 10, sort = "date") Pageable pageable
    ) {
        boolean useNative = "native".equalsIgnoreCase(queryType);

        Page<MatchDto> result = useNative
            ? matchService.findMatchesNative(filter, pageable.getPageNumber(), pageable.getPageSize())
            : matchService.findMatches(filter, pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(result);
    }
}