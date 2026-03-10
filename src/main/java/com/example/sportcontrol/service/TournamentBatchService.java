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

@Service
@RequiredArgsConstructor
public class TournamentBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(TournamentBatchService.class);

    private final SportRepository sportRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public void createTournamentWithMatches(TournamentWithMatchesDto dto) {
        Tournament tournament = saveTournamentWithMatches(dto);
        LOG.info("Tournament id={} with {} matches saved (no @Transactional)",
                tournament.getId(), dto.getMatches().size());
    }
    
    @Transactional
    public void createTournamentWithMatchesTransactional(TournamentWithMatchesDto dto) {
        Tournament tournament = saveTournamentWithMatches(dto);
        LOG.info("Tournament id={} with {} matches saved (@Transactional)",
                tournament.getId(), dto.getMatches().size());
    }

    private Tournament saveTournamentWithMatches(TournamentWithMatchesDto dto) {
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sport not found: " + dto.getSportId()));

        Tournament tournament = new Tournament();
        tournament.setName(dto.getTournamentName());
        tournament.setSlug(dto.getTournamentSlug());
        tournament.setSport(sport);
        tournamentRepository.save(tournament);
        LOG.info("Tournament saved with id={}", tournament.getId());

        for (int i = 0; i < dto.getMatches().size(); i++) {
            MatchDto matchDto = dto.getMatches().get(i);

            Team homeTeam = teamRepository.findById(matchDto.getHomeTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Team not found: " + matchDto.getHomeTeamId()));
            Team awayTeam = teamRepository.findById(matchDto.getAwayTeamId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Team not found: " + matchDto.getAwayTeamId()));

            Match match = new Match();
            match.setName(matchDto.getName());
            match.setLocation(matchDto.getLocation());
            match.setDate(matchDto.getDate());
            match.setTournament(tournament);
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);
            matchRepository.save(match);
            LOG.info("Match #{} saved with id={}", i + 1, match.getId());
        }

        return tournament;
    }
}
