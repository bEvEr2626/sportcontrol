package com.example.sportcontrol.service;

import java.util.List;
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
        return teamRepository.findById(id)
            .map(teamMapper::toDto)
            .orElseThrow(() -> {
                LOG.warn("Team not found: {}", id);
                return new NoSuchElementException("Team " + id + " not found");
            });
    }

    public TeamDto create(TeamDto dto) {
        LOG.info("Creating team: {}", dto);
        Team entity = teamMapper.toEntity(dto);
        Team saved = teamRepository.save(entity);
        LOG.info("Team created with id={}", saved.getId());
        return teamMapper.toDto(saved);
    }

    public TeamDto update(Long id, TeamDto dto) {
        LOG.info("Updating team id={} with data {}", id, dto);
        Team existing = teamRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Team not found for update: {}", id);
                return new NoSuchElementException("Team " + id + " not found");
            });
        existing.setName(dto.getName());
        Team saved = teamRepository.save(existing);
        LOG.info("Team updated with id={}", saved.getId());
        return teamMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting team with id={}", id);
        teamRepository.deleteById(id);
        LOG.info("Team deleted: {}", id);
    }
}
