package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import java.util.NoSuchElementException;
import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchService.class);

    private static final String TEAM_NOT_FOUND = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND = "Tournament not found: ";

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    private final Map<MatchSearchKey, Page<MatchDto>> cache = new HashMap<>();

    @Transactional(readOnly = true)
    public Page<MatchDto> findMatches(MatchFilter filter, int page, int size) {
        LOG.debug("findMatches called with filter={}, page={}, size={}", filter, page, size);
        MatchSearchKey key = new MatchSearchKey(filter, page, size);
        if (cache.containsKey(key)) {
            LOG.debug("Returning cached result for key={}", key);
            return cache.get(key);
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<MatchDto> result = matchRepository.findWithFilters(filter, pageable)
                .map(matchMapper::toDto);
        cache.put(key, result);
        LOG.info("Found {} matches for filter={}", result.getTotalElements(), filter);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<MatchDto> findMatchesNative(MatchFilter filter, int page, int size) {
        LOG.debug("findMatchesNative called with filter={}, page={}, size={}", filter, page, size);
        MatchSearchKey key = new MatchSearchKey(filter, page, size);
        if (cache.containsKey(key)) {
            LOG.debug("Returning cached native result for key={}", key);
            return cache.get(key);
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<MatchDto> result = matchRepository.findWithFiltersNative(filter, pageable)
                .map(matchMapper::toDto);
        cache.put(key, result);
        LOG.info("Found {} matches (native) for filter={}", result.getTotalElements(), filter);
        return result;
    }

    @Transactional(readOnly = true)
    public MatchDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return matchRepository.findById(id)
            .map(matchMapper::toDto)
            .orElseThrow(() -> {
                LOG.warn("Match not found: {}", id);
                return new NoSuchElementException("Match not found: " + id);
            });
    }

    @Transactional
    public MatchDto create(MatchDto dto) {
        LOG.info("Creating match: {}", dto);
        Match entity = matchMapper.toEntity(dto);
        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> {
                    LOG.warn("Tournament not found: {}", dto.getTournamentId());
                    return new NoSuchElementException(TOURNAMENT_NOT_FOUND + dto.getTournamentId());
                });
            entity.setTournament(tournament);
        }
        if (dto.getHomeTeamId() != null) {
            Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                .orElseThrow(() -> {
                    LOG.warn("Home team not found: {}", dto.getHomeTeamId());
                    return new NoSuchElementException(TEAM_NOT_FOUND + dto.getHomeTeamId());
                });
            entity.setHomeTeam(homeTeam);
        }
        if (dto.getAwayTeamId() != null) {
            Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                .orElseThrow(() -> {
                    LOG.warn("Away team not found: {}", dto.getAwayTeamId());
                    return new NoSuchElementException(TEAM_NOT_FOUND + dto.getAwayTeamId());
                });
            entity.setAwayTeam(awayTeam);
        }
        Match saved = matchRepository.save(entity);
        cache.clear();
        LOG.info("Match created with id={}", saved.getId());
        return matchMapper.toDto(saved);
    }

    @Transactional
    public MatchDto update(Long id, MatchDto dto) {
        LOG.info("Updating match id={} with data {}", id, dto);
        Match existing = matchRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Match not found for update: {}", id);
                return new NoSuchElementException("Match not found: " + id);
            });
        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setDate(dto.getDate());
        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> {
                    LOG.warn("Tournament not found: {}", dto.getTournamentId());
                    return new NoSuchElementException(TOURNAMENT_NOT_FOUND + dto.getTournamentId());
                });
            existing.setTournament(tournament);
        }
        if (dto.getHomeTeamId() != null) {
            Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                .orElseThrow(() -> {
                    LOG.warn("Home team not found: {}", dto.getHomeTeamId());
                    return new NoSuchElementException(TEAM_NOT_FOUND + dto.getHomeTeamId());
                });
            existing.setHomeTeam(homeTeam);
        }
        if (dto.getAwayTeamId() != null) {
            Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                .orElseThrow(() -> {
                    LOG.warn("Away team not found: {}", dto.getAwayTeamId());
                    return new NoSuchElementException(TEAM_NOT_FOUND + dto.getAwayTeamId());
                });
            existing.setAwayTeam(awayTeam);
        }
        Match saved = matchRepository.save(existing);
        cache.clear();
        LOG.info("Match updated with id={}", saved.getId());
        return matchMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        LOG.info("Deleting match with id={}", id);
        matchRepository.deleteById(id);
        cache.clear();
        LOG.info("Match deleted: {}", id);
    }

    @Transactional(readOnly = true)
    public java.util.List<MatchDto> getAll() {
        LOG.info("Getting all matches");
        return matchRepository.findAllBy().stream()
                .map(matchMapper::toDto)
                .toList();
    }
}