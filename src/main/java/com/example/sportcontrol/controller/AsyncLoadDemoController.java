package com.example.sportcontrol.controller;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import com.example.sportcontrol.service.AsyncLoadDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async-load")
@RequiredArgsConstructor
@Tag(name = "Concurrency", description = "Async load demo operations")
public class AsyncLoadDemoController {

    private final AsyncLoadDemoService asyncLoadDemoService;

    @PostMapping("/start")
    @Operation(
        summary = "Start async load demo",
        description = "Starts async load demo task and returns task ID"
    )
    public ResponseEntity<AsyncTaskAcceptedResponseDto> startTask(
        @Parameter(description = "Task duration in milliseconds", example = "5000")
        @RequestParam(defaultValue = "5000") long durationMs
    ) {
        return ResponseEntity.accepted().body(asyncLoadDemoService.startTask(durationMs));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Get async load demo status", description = "Returns status of async load demo task")
    public ResponseEntity<AsyncTaskStatusDto> getTaskStatus(@PathVariable String taskId) {
        return ResponseEntity.ok(asyncLoadDemoService.getTaskStatus(taskId));
    }
}
