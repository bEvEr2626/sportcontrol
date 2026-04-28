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
@RequestMapping("/concurrency")
@RequiredArgsConstructor
@Tag(name = "Concurrency", description = "Race condition and async load demo")
public class AsyncLoadDemoController {

    private final AsyncLoadDemoService asyncLoadDemoService;

    @PostMapping("/async-demo")
    @Operation(summary = "Start async demo task without database writes")
    public ResponseEntity<AsyncTaskAcceptedResponseDto> startAsyncDemo(
        @Parameter(description = "Task duration in milliseconds", example = "5000")
        @RequestParam(defaultValue = "5000") long durationMs
    ) {
        return ResponseEntity.accepted().body(asyncLoadDemoService.startTask(durationMs));
    }

    @GetMapping("/async-demo/tasks/{taskId}")
    @Operation(summary = "Get async demo task status")
    public ResponseEntity<AsyncTaskStatusDto> getAsyncDemoStatus(@PathVariable String taskId) {
        return ResponseEntity.ok(asyncLoadDemoService.getTaskStatus(taskId));
    }
}
