package com.example.sportcontrol.service;

import com.example.sportcontrol.cache.MatchSearchKey;
import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import java.util.NoSuchElementException;
import java.util.Optional;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchService.class);

    private static final String MATCH_NOT_FOUND = "Match not found: ";
    private static final String TEAM_NOT_FOUND = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND = "Tournament not found: ";
    private static final String TOURNAMENT_NOT_FOUND_LOG = "Tournament not found: {}";
    private static final String HOME_TEAM_NOT_FOUND_LOG = "Home team not found: {}";
    private static final String AWAY_TEAM_NOT_FOUND_LOG = "Away team not found: {}";

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
                return new NoSuchElementException(MATCH_NOT_FOUND + id);
            });
    }

    @Transactional
    public MatchDto create(MatchDto dto) {
        LOG.info("Creating match: {}", dto);
        Match entity = matchMapper.toEntity(dto);
        Optional.ofNullable(dto.getTournamentId())
            .map(this::findTournamentById)
            .ifPresent(entity::setTournament);
        Optional.ofNullable(dto.getHomeTeamId())
            .map(id -> findTeamById(id, HOME_TEAM_NOT_FOUND_LOG))
            .ifPresent(entity::setHomeTeam);
        Optional.ofNullable(dto.getAwayTeamId())
            .map(id -> findTeamById(id, AWAY_TEAM_NOT_FOUND_LOG))
            .ifPresent(entity::setAwayTeam);
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
                return new NoSuchElementException(MATCH_NOT_FOUND + id);
            });
        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setDate(dto.getDate());
        Optional.ofNullable(dto.getTournamentId())
            .map(this::findTournamentById)
            .ifPresent(existing::setTournament);
        Optional.ofNullable(dto.getHomeTeamId())
            .map(teamId -> findTeamById(teamId, HOME_TEAM_NOT_FOUND_LOG))
            .ifPresent(existing::setHomeTeam);
        Optional.ofNullable(dto.getAwayTeamId())
            .map(teamId -> findTeamById(teamId, AWAY_TEAM_NOT_FOUND_LOG))
            .ifPresent(existing::setAwayTeam);
        Match saved = matchRepository.save(existing);
        cache.clear();
        LOG.info("Match updated with id={}", saved.getId());
        return matchMapper.toDto(saved);
    }

    @Transactional
    public MatchDto patch(Long id, MatchDto dto) {
        LOG.info("Patching match id={} with data {}", id, dto);
        Match existing = matchRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Match not found for patch: {}", id);
                return new NoSuchElementException(MATCH_NOT_FOUND + id);
            });

        Optional.ofNullable(dto.getName()).ifPresent(existing::setName);
        Optional.ofNullable(dto.getLocation()).ifPresent(existing::setLocation);
        Optional.ofNullable(dto.getDate()).ifPresent(existing::setDate);
        Optional.ofNullable(dto.getTournamentId())
            .map(this::findTournamentById)
            .ifPresent(existing::setTournament);
        Optional.ofNullable(dto.getHomeTeamId())
            .map(teamId -> findTeamById(teamId, HOME_TEAM_NOT_FOUND_LOG))
            .ifPresent(existing::setHomeTeam);
        Optional.ofNullable(dto.getAwayTeamId())
            .map(teamId -> findTeamById(teamId, AWAY_TEAM_NOT_FOUND_LOG))
            .ifPresent(existing::setAwayTeam);

        Match saved = matchRepository.save(existing);
        cache.clear();
        LOG.info("Match patched with id={}", saved.getId());
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

    @Transactional
    public List<MatchDto> bulkCreateTransactional(List<MatchDto> matches) {
        List<MatchDto> safeMatches = Optional.ofNullable(matches)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Matches list cannot be empty"));
        
        List<MatchDto> result = safeMatches.stream()
            .map(this::create)
            .toList();
        LOG.info("Bulk operation completed size={}", result.size());
        return result;
    }

    public List<MatchDto> bulkCreateNoTransactional(List<MatchDto> matches) {
        List<MatchDto> safeMatches = Optional.ofNullable(matches)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Matches list cannot be empty"));

        LOG.info("Starting bulk match operation WITHOUT transaction size={}", safeMatches.size());

        List<MatchDto> result = new ArrayList<>();
        Map<String, String> failedMatches = new LinkedHashMap<>();

        IntStream.range(0, safeMatches.size()).forEach(index -> {
            MatchDto dto = safeMatches.get(index);
            try {
                LOG.info("Creating match: {}", dto);
                Match entity = matchMapper.toEntity(dto);
                Optional.ofNullable(dto.getTournamentId())
                    .map(this::findTournamentById)
                    .ifPresent(entity::setTournament);
                Optional.ofNullable(dto.getHomeTeamId())
                    .map(teamId -> findTeamById(teamId, HOME_TEAM_NOT_FOUND_LOG))
                    .ifPresent(entity::setHomeTeam);
                Optional.ofNullable(dto.getAwayTeamId())
                    .map(teamId -> findTeamById(teamId, AWAY_TEAM_NOT_FOUND_LOG))
                    .ifPresent(entity::setAwayTeam);

                Match saved = matchRepository.save(entity);
                cache.clear();
                LOG.info("Match created with id={}", saved.getId());
                result.add(matchMapper.toDto(saved));
            } catch (RuntimeException ex) {
                String key = "match_%d".formatted(index + 1);
                String failureMessage = ex.getMessage() == null
                    ? ex.getClass().getSimpleName()
                    : ex.getMessage();
                failedMatches.put(key, failureMessage);
                LOG.warn("Skipping failed match in non-transactional bulk {}: {}", key, failureMessage);
            }
        });

        if (!failedMatches.isEmpty()) {
            throw new IllegalStateException(
                "Some matches were not saved. successCount=%d, failedCount=%d, failedMatches=%s"
                    .formatted(result.size(), failedMatches.size(), failedMatches)
            );
        }

        LOG.info("Bulk match operation WITHOUT transaction completed size={}", result.size());
        return result;
    }

    private Tournament findTournamentById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> {
                LOG.warn(TOURNAMENT_NOT_FOUND_LOG, tournamentId);
                return new NoSuchElementException(TOURNAMENT_NOT_FOUND + tournamentId);
            });
    }

    private Team findTeamById(Long teamId, String notFoundLogTemplate) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> {
                LOG.warn(notFoundLogTemplate, teamId);
                return new NoSuchElementException(TEAM_NOT_FOUND + teamId);
            });
    }
}