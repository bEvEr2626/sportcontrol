package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import com.example.sportcontrol.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final String TEAM_NOT_FOUND = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND = "Tournament not found: ";

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    @Transactional(readOnly = true)
    public List<MatchDto> getAll() {
        return matchRepository.findAllBy().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MatchDto> getAllNPlusOne() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    public MatchDto create(MatchDto dto) {
        Match entity = matchMapper.toEntity(dto);
        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository
                    .findById(dto.getTournamentId())
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
        Match savedEntity = matchRepository.save(entity);
        return matchMapper.toDto(savedEntity);
    }

    public MatchDto getById(Long id) {
        return matchRepository.findById(id)
                .map(matchMapper::toDto)
                .orElse(null);
    }

    public List<MatchDto> getMatchesByLocation(String location) {
        return matchRepository.findByLocation(location)
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }

    public MatchDto update(Long id, MatchDto dto) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Match not found: " + id));
        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setDate(dto.getDate());
        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository
                    .findById(dto.getTournamentId())
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
        return matchMapper.toDto(matchRepository.save(existing));
    }

    public void delete(Long id) {
        matchRepository.deleteById(id);
    }
}
