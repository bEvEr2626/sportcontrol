package com.example.sportcontrol.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.entity.Team;
import java.util.NoSuchElementException;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.mapper.TournamentMapper;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private static final Logger LOG = LoggerFactory.getLogger(TournamentService.class);
    private final TournamentRepository tournamentRepository;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    private final TournamentMapper tournamentMapper;

    public List<TournamentDto> getAllTournaments() {
        LOG.info("Getting all tournaments");
        return tournamentRepository.findAllBy().stream()
                .map(tournamentMapper::toDto)
                .toList();
    }


    public TournamentDto create(TournamentDto dto) {
        TournamentDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Tournament payload cannot be null"));
        LOG.info("Creating tournament: {}", safeDto);
        Tournament entity = tournamentMapper.toEntity(safeDto);
        Long sportId = Optional.ofNullable(safeDto.getSportId())
            .orElseThrow(() -> new IllegalArgumentException("Sport id is required for tournament creation"));
        Sport sport = findSportById(sportId);
        entity.setSport(sport);
        Tournament savedEntity = tournamentRepository.save(entity);
        LOG.info("Tournament created with id={}", savedEntity.getId());
        return tournamentMapper.toDto(savedEntity);
    }

    public TournamentDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return tournamentMapper.toDto(findTournamentById(id));
    }

    public TournamentDto update(Long id, TournamentDto dto) {
        TournamentDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Tournament payload cannot be null"));
        LOG.info("Updating tournament id={} with data {}", id, safeDto);
        Tournament existing = findTournamentById(id);
        existing.setName(safeDto.getName());
        Optional.ofNullable(safeDto.getSportId())
            .map(this::findSportById)
            .ifPresent(existing::setSport);
        Tournament saved = tournamentRepository.save(existing);
        LOG.info("Tournament updated with id={}", saved.getId());
        return tournamentMapper.toDto(saved);
    }

    @Transactional
    public TournamentDto addTeams(Long tournamentId, List<Long> teamIds) {
        List<Long> safeTeamIds = Optional.ofNullable(teamIds)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Team ids cannot be empty"));

        for (Long teamId : safeTeamIds) {
            if (teamId == null) {
                throw new IllegalArgumentException("Team ids cannot contain null values");
            }
        }

        Tournament tournament = findTournamentById(tournamentId);
        List<Team> teams = teamRepository.findAllById(safeTeamIds);
        Set<Long> foundIds = teams.stream()
            .map(Team::getId)
            .collect(Collectors.toSet());

        List<Long> missingIds = new ArrayList<>();
        Set<Long> seenIds = new HashSet<>();
        for (Long teamId : safeTeamIds) {
            if (!foundIds.contains(teamId) && seenIds.add(teamId)) {
                missingIds.add(teamId);
            }
        }

        if (!missingIds.isEmpty()) {
            LOG.warn("Teams not found for tournament {}: {}", tournamentId, missingIds);
            throw new NoSuchElementException("Teams not found: " + missingIds);
        }

        int addedCount = 0;
        for (Team team : teams) {
            if (!tournament.getTeams().contains(team)) {
                tournament.getTeams().add(team);
                addedCount++;
            }
            if (!team.getTournaments().contains(tournament)) {
                team.getTournaments().add(tournament);
            }
        }

        Tournament saved = tournamentRepository.save(tournament);
        LOG.info("Added {} teams to tournament id={}", addedCount, saved.getId());
        return tournamentMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting tournament with id={}", id);
        tournamentRepository.deleteById(id);
        LOG.info("Tournament deleted: {}", id);
    }

    private Tournament findTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> {
                LOG.warn("Tournament not found: {}", tournamentId);
                return new NoSuchElementException("Tournament " + tournamentId + " not found");
            });
    }

    private Sport findSportById(Long sportId) {
        return sportRepository.findById(sportId)
            .orElseThrow(() -> {
                LOG.warn("Sport not found: {}", sportId);
                return new NoSuchElementException("Sport not found: " + sportId);
            });
    }

    @Transactional
    public void removeTeams(Long tournamentId, List<Long> teamIds) {
    Tournament tournament = findTournamentById(tournamentId);
    List<Team> teamsToRemove = teamRepository.findAllById(teamIds);
    if (teamsToRemove.size() != teamIds.size()) {
        Set<Long> found = teamsToRemove.stream().map(Team::getId).collect(Collectors.toSet());
        List<Long> missing = teamIds.stream().filter(id -> !found.contains(id)).toList();
        throw new NoSuchElementException("Teams not found: " + missing);
    }
    teamsToRemove.forEach(team -> {
        tournament.getTeams().remove(team);
        team.getTournaments().remove(tournament);
    });
    tournamentRepository.save(tournament);
    LOG.info("Removed {} teams from tournament {}", teamsToRemove.size(), tournamentId);
}
}
