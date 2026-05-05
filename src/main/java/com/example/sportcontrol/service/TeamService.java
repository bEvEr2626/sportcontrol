package com.example.sportcontrol.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import java.util.NoSuchElementException;
import com.example.sportcontrol.mapper.TeamMapper;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
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

    @Transactional
    public void delete(Long id) {
        LOG.info("Deleting team with id={}", id);
        Team team = findTeamById(id);
        detachFromTournaments(team);
        long deletedMatches = matchRepository.deleteByHomeTeam_IdOrAwayTeam_Id(id, id);
        LOG.info("Deleted {} matches for team id={}", deletedMatches, id);
        teamRepository.delete(team);
        LOG.info("Team deleted: {}", id);
    }

    private void detachFromTournaments(Team team) {
        List<Tournament> tournaments = new ArrayList<>(team.getTournaments());
        for (Tournament tournament : tournaments) {
            tournament.getTeams().remove(team);
        }
        team.getTournaments().clear();
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Team not found: {}", id);
                return new NoSuchElementException("Team " + id + " not found");
            });
    }
}
