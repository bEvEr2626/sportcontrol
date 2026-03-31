package com.example.sportcontrol.service;

import java.util.List;
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
        LOG.info("Creating tournament: {}", dto);
        Tournament entity = tournamentMapper.toEntity(dto);
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> {
                    LOG.warn("Sport not found: {}", dto.getSportId());
                    return new NoSuchElementException("Sport not found with id: " + dto.getSportId());
                });
        entity.setSport(sport);
        Tournament savedEntity = tournamentRepository.save(entity);
        LOG.info("Tournament created with id={}", savedEntity.getId());
        return tournamentMapper.toDto(savedEntity);
    }

    public TournamentDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return tournamentRepository.findById(id)
            .map(tournamentMapper::toDto)
            .orElseThrow(() -> {
                LOG.warn("Tournament not found: {}", id);
                return new NoSuchElementException("Tournament " + id + " not found");
            });
    }

    public TournamentDto update(Long id, TournamentDto dto) {
        LOG.info("Updating tournament id={} with data {}", id, dto);
        Tournament existing = tournamentRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Tournament not found for update: {}", id);
                return new NoSuchElementException("Tournament " + id + " not found");
            });
        existing.setName(dto.getName());
        if (dto.getSportId() != null) {
            Sport sport = sportRepository.findById(dto.getSportId())
                    .orElseThrow(() -> {
                        LOG.warn("Sport not found: {}", dto.getSportId());
                        return new NoSuchElementException("Sport not found: " + dto.getSportId());
                    });
            existing.setSport(sport);
        }
        Tournament saved = tournamentRepository.save(existing);
        LOG.info("Tournament updated with id={}", saved.getId());
        return tournamentMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting tournament with id={}", id);
        tournamentRepository.deleteById(id);
        LOG.info("Tournament deleted: {}", id);
    }
}
