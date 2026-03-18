package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.TournamentWithMatchesDto;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(TournamentBatchService.class);

    private final SportRepository sportRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Tournament createTournamentWithMatches(TournamentWithMatchesDto dto) {
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sport not found: " + dto.getSportId()));

        Tournament tournament = new Tournament();
        tournament.setName(dto.getTournamentName());
        tournament.setSlug(dto.getTournamentSlug());
        tournament.setSport(sport);

        List<Long> teamIds = dto.getMatches().stream()
                .flatMap(m -> List.of(m.getHomeTeamId(), m.getAwayTeamId()).stream())
                .distinct()
                .toList();

        List<Team> teams = teamRepository.findAllById(teamIds);
        if (teams.size() != teamIds.size()) {
            throw new EntityNotFoundException("Some teams not found");
        }

        tournament.setTeams(teams);

        Tournament savedTournament = tournamentRepository.save(tournament);
        LOG.info("Tournament saved with id={}", savedTournament.getId());

        for (MatchDto matchDto : dto.getMatches()) {
            Team homeTeam = teams.stream()
                    .filter(t -> t.getId().equals(matchDto.getHomeTeamId()))
                    .findFirst().orElseThrow();
            Team awayTeam = teams.stream()
                    .filter(t -> t.getId().equals(matchDto.getAwayTeamId()))
                    .findFirst().orElseThrow();

            Match match = new Match();
            match.setName(matchDto.getName());
            match.setLocation(matchDto.getLocation());
            match.setDate(matchDto.getDate());
            match.setTournament(savedTournament);
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);

            matchRepository.save(match);
            LOG.info("Match '{}' saved with id={}", match.getName(), match.getId());
        }

        return savedTournament;
    }
}