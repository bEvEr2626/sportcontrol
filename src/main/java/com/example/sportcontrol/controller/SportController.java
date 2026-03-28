
package com.example.sportcontrol.controller;

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
public class SportController {
    private final SportService sportService;

    @PatchMapping("/{id}")
    public SportDto patch(@PathVariable Long id, @RequestBody @Valid SportDto dto) {
        return sportService.update(id, dto);
    }

    @GetMapping
    public List<SportDto> getAll() {
        return sportService.getAllSports();
    }
    
    @PostMapping
    public SportDto create(@RequestBody @Valid SportDto dto) {
        return sportService.create(dto);
    }

    @GetMapping("/{id}")
    public SportDto getById(@PathVariable Long id) {
        return sportService.getById(id);
    }

    @PutMapping("/{id}")
    public SportDto update(@PathVariable Long id, @RequestBody SportDto dto) {
        return sportService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sportService.delete(id);
    }
}
