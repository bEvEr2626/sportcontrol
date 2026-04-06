
package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sportcontrol.dto.SportDto;
import jakarta.validation.Valid;
import com.example.sportcontrol.service.SportService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/sports")
@RequiredArgsConstructor
@Tag(name = "Sports", description = "Operations for managing sports")
public class SportController {
    private final SportService sportService;

    @PatchMapping("/{id}")
    @Operation(summary = "Patch sport", description = "Partially updates an existing sport by its ID")
    public SportDto patch(@PathVariable Long id, @RequestBody @Valid SportDto dto) {
        return sportService.update(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get all sports", description = "Returns a list of all sports")
    public List<SportDto> getAll() {
        return sportService.getAllSports();
    }
    
    @PostMapping
    @Operation(summary = "Create sport", description = "Creates a new sport")
    public SportDto create(@RequestBody @Valid SportDto dto) {
        return sportService.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sport by ID", description = "Returns a sport by its ID")
    public SportDto getById(@PathVariable Long id) {
        return sportService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sport", description = "Fully updates an existing sport by its ID")
    public SportDto update(@PathVariable Long id, @RequestBody SportDto dto) {
        return sportService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sport", description = "Deletes a sport by its ID")
    public void delete(@PathVariable Long id) {
        sportService.delete(id);
    }
}
