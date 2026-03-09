package com.example.sportcontrol.service;

import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import com.example.sportcontrol.exception.SimulatedErrorException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalDemoService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionalDemoService.class);

    private final SportRepository sportRepository;
    private final TournamentRepository tournamentRepository;

    public void saveWithoutTransactional() {
        Sport sport = new Sport();
        sport.setName("Demo Sport (no tx)");
        sport.setSlug("demo-sport-no-tx");
        sportRepository.save(sport);
        LOG.info("Sport saved with id={}", sport.getId());

        Tournament tournament = new Tournament();
        tournament.setName("Demo Tournament (no tx)");
        tournament.setSlug("demo-tournament-no-tx");
        tournament.setSport(sport);
        tournamentRepository.save(tournament);
        LOG.info("Tournament saved with id={}", tournament.getId());

        LOG.info("Throwing exception AFTER both saves...");
        throw new SimulatedErrorException(
                "Simulated error: Sport (id=" + sport.getId()
                        + ") is SAVED, Tournament (id=" + tournament.getId()
                        + ") is SAVED — partial data remains in DB!");
    }

    @Transactional
    public void saveWithTransactional() {
        Sport sport = new Sport();
        sport.setName("Demo Sport (with tx)");
        sport.setSlug("demo-sport-with-tx");
        sportRepository.save(sport);
        LOG.info("Sport saved with id={}", sport.getId());

        Tournament tournament = new Tournament();
        tournament.setName("Demo Tournament (with tx)");
        tournament.setSlug("demo-tournament-with-tx");
        tournament.setSport(sport);
        tournamentRepository.save(tournament);
        LOG.info("Tournament saved with id={}", tournament.getId());

        LOG.info("Throwing exception AFTER both saves...");
        throw new SimulatedErrorException(
                "Simulated error: both Sport and Tournament will be ROLLED BACK!");
    }
}
