
package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import com.example.sportcontrol.dto.MatchDto;
import jakarta.validation.Valid;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.service.MatchAsyncTaskService;
import com.example.sportcontrol.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Operations for managing matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchAsyncTaskService matchAsyncTaskService;

    @PatchMapping("/{id:\\d+}")
    @Operation(summary = "Patch match", description = "Partially updates an existing match by ID")
    public MatchDto patch(@PathVariable Long id, @RequestBody MatchDto dto) {
        return matchService.patch(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get matches", description = "Returns a paginated list of matches")
    public Page<MatchDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return matchService.findMatches(new MatchFilter(), page, size);
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Get match by ID", description = "Returns a match by ID")
    public MatchDto getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create match", description = "Creates a new match")
    public MatchDto create(@RequestBody @Valid MatchDto dto) {
        return matchService.create(dto);
    }

    @PutMapping("/{id:\\d+}")
    @Operation(summary = "Update match", description = "Fully updates an existing match by ID")
    public MatchDto update(@PathVariable Long id, @RequestBody @Valid MatchDto dto) {
        return matchService.update(id, dto);
    }

    @DeleteMapping("/{id:\\d+}")
    @Operation(summary = "Delete match", description = "Deletes a match by ID")
    public void delete(@PathVariable Long id) {
        matchService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search matches", description = "Searches matches by filter and query type (jpql/native)")
    public ResponseEntity<Page<MatchDto>> search(
        @ModelAttribute @Valid MatchFilter filter,
        @RequestParam(defaultValue = "jpql") String queryType,
        @PageableDefault(size = 10, sort = "date") Pageable pageable
    ) {
        boolean useNative = "native".equalsIgnoreCase(queryType);

        Page<MatchDto> result = useNative
            ? matchService.findMatchesNative(filter, pageable.getPageNumber(), pageable.getPageSize())
            : matchService.findMatches(filter, pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/bulk")
    @Operation(summary = "Bulk create matches", description = "Creates multiple matches without wrapping in a single transaction")
    public ResponseEntity<List<MatchDto>> bulkCreate(
        @RequestBody List<MatchDto> matches) {
        return ResponseEntity.ok(matchService.bulkCreateNoTransactional(matches));
    }

    @PostMapping("/bulk/tx")
    @Operation(summary = "Bulk create matches (transactional)", description = "Creates multiple matches in a single transaction")
    public ResponseEntity<List<MatchDto>> bulkCreateTransactional(
        @RequestBody @Valid List<MatchDto> matches) {
        return ResponseEntity.ok(matchService.bulkCreateTransactional(matches));
    }

    @PostMapping("/bulk/async")
    @Operation(
        summary = "Start async bulk create",
        description = "Starts asynchronous transactional bulk creation and returns task ID"
    )
    public ResponseEntity<AsyncTaskAcceptedResponseDto> bulkCreateAsync(
        @RequestBody List<MatchDto> matches) {
        return ResponseEntity.accepted().body(matchAsyncTaskService.startBulkCreateTask(matches));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Get async task status", description = "Returns status of asynchronous bulk creation task")
    public ResponseEntity<AsyncTaskStatusDto> getTaskStatus(@PathVariable String taskId) {
        return ResponseEntity.ok(matchAsyncTaskService.getTaskStatus(taskId));
    }
}