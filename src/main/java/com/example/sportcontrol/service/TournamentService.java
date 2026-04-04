package com.example.sportcontrol.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Sport;
import java.util.NoSuchElementException;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.mapper.TournamentMapper;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private static final Logger LOG = LoggerFactory.getLogger(TournamentService.class);
    private final TournamentRepository tournamentRepository;
    private final SportRepository sportRepository;
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
}
