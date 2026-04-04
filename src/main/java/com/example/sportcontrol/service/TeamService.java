package com.example.sportcontrol.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.entity.Team;
import java.util.NoSuchElementException;
import com.example.sportcontrol.mapper.TeamMapper;
import com.example.sportcontrol.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public List<TeamDto> getAll() {
        LOG.info("Getting all teams");
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    public TeamDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return teamMapper.toDto(findTeamById(id));
    }

    public TeamDto create(TeamDto dto) {
        TeamDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Team payload cannot be null"));
        LOG.info("Creating team: {}", safeDto);
        Team entity = teamMapper.toEntity(safeDto);
        Team saved = teamRepository.save(entity);
        LOG.info("Team created with id={}", saved.getId());
        return teamMapper.toDto(saved);
    }

    public TeamDto update(Long id, TeamDto dto) {
        TeamDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Team payload cannot be null"));
        LOG.info("Updating team id={} with data {}", id, safeDto);
        Team existing = findTeamById(id);
        existing.setName(safeDto.getName());
        Team saved = teamRepository.save(existing);
        LOG.info("Team updated with id={}", saved.getId());
        return teamMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting team with id={}", id);
        teamRepository.deleteById(id);
        LOG.info("Team deleted: {}", id);
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Team not found: {}", id);
                return new NoSuchElementException("Team " + id + " not found");
            });
    }
}
