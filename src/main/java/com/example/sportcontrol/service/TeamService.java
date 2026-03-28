package com.example.sportcontrol.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.entity.Team;
import java.util.NoSuchElementException;
import com.example.sportcontrol.mapper.TeamMapper;
import com.example.sportcontrol.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamDto getById(Long id) {
        return teamRepository.findById(id)
            .map(teamMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
    }

    public TeamDto create(TeamDto dto) {
        Team entity = teamMapper.toEntity(dto);
        return teamMapper.toDto(teamRepository.save(entity));
    }

    public TeamDto update(Long id, TeamDto dto) {
        Team existing = teamRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
        existing.setName(dto.getName());
        return teamMapper.toDto(teamRepository.save(existing));
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }
}
