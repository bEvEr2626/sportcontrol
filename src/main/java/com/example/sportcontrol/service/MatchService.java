package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final String TEAM_NOT_FOUND = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND = "Tournament not found: ";

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    private final Map<MatchSearchKey, List<MatchDto>> cache = new HashMap<>();

    @Transactional(readOnly = true)
    public List<MatchDto> getAll() {
        return matchRepository.findAllBy().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MatchDto getById(Long id) {
        return matchRepository.findById(id)
                .map(matchMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public MatchDto create(MatchDto dto) {
        Match entity = matchMapper.toEntity(dto);

        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TOURNAMENT_NOT_FOUND + dto.getTournamentId()));
            entity.setTournament(tournament);
        }

        if (dto.getHomeTeamId() != null) {
            Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TEAM_NOT_FOUND + dto.getHomeTeamId()));
            entity.setHomeTeam(homeTeam);
        }

        if (dto.getAwayTeamId() != null) {
            Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TEAM_NOT_FOUND + dto.getAwayTeamId()));
            entity.setAwayTeam(awayTeam);
        }

        Match saved = matchRepository.save(entity);

        cache.clear(); 

        return matchMapper.toDto(saved);
    }

    @Transactional
    public MatchDto update(Long id, MatchDto dto) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found: " + id));

        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setDate(dto.getDate());

        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TOURNAMENT_NOT_FOUND + dto.getTournamentId()));
            existing.setTournament(tournament);
        }

        if (dto.getHomeTeamId() != null) {
            Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TEAM_NOT_FOUND + dto.getHomeTeamId()));
            existing.setHomeTeam(homeTeam);
        }

        if (dto.getAwayTeamId() != null) {
            Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            TEAM_NOT_FOUND + dto.getAwayTeamId()));
            existing.setAwayTeam(awayTeam);
        }

        Match saved = matchRepository.save(existing);

        cache.clear(); 

        return matchMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        matchRepository.deleteById(id);
        cache.clear(); 
    }

    @Transactional(readOnly = true)
    public Page<MatchDto> findMatches(MatchFilter filter, int page, int size) {
        MatchSearchKey key = new MatchSearchKey(
                filter.getName(),
                filter.getLocation(),
                filter.getTournamentId(),
                filter.getHomeTeamName(),
                filter.getAwayTeamName(),
                filter.getDateFrom(),
                filter.getDateTo(),
                page,
                size
        );

        if (cache.containsKey(key)) {
            return toPage(cache.get(key), page, size);
        }

        PageRequest pageable = PageRequest.of(page, size);

        List<MatchDto> result = matchRepository.findWithFilters(filter, pageable)
                .map(matchMapper::toDto)
                .toList();

        cache.put(key, result);

        return toPage(result, page, size);
    }

    @Transactional(readOnly = true)
    public Page<MatchDto> findMatchesNative(MatchFilter filter, int page, int size) {
        MatchSearchKey key = new MatchSearchKey(
                filter.getName(),
                filter.getLocation(),
                filter.getTournamentId(),
                filter.getHomeTeamName(),
                filter.getAwayTeamName(),
                filter.getDateFrom(),
                filter.getDateTo(),
                page,
                size
        );

        if (cache.containsKey(key)) {
            return toPage(cache.get(key), page, size);
        }

        PageRequest pageable = PageRequest.of(page, size);

        List<MatchDto> result = matchRepository.findWithFiltersNative(filter, pageable)
                .map(matchMapper::toDto)
                .toList();

        cache.put(key, result);

        return toPage(result, page, size);
    }

    private Page<MatchDto> toPage(List<MatchDto> list, int page, int size) {
        int start = Math.min(page * size, list.size());
        int end = Math.min(start + size, list.size());
        List<MatchDto> subList = list.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(subList, PageRequest.of(page, size), list.size());
    }
}