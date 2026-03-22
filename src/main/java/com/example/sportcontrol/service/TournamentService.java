package com.example.sportcontrol.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.mapper.TournamentMapper;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final SportRepository sportRepository;
    private final TournamentMapper tournamentMapper;

    public List<TournamentDto> getAllTournaments() {
        return tournamentRepository.findAllBy().stream()
                .map(tournamentMapper::toDto)
                .toList();
    }


    public TournamentDto create(TournamentDto dto) {
        Tournament entity = tournamentMapper.toEntity(dto);
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + dto.getSportId()));
        
        entity.setSport(sport);
        Tournament savedEntity = tournamentRepository.save(entity);
        return tournamentMapper.toDto(savedEntity);
    }

    public TournamentDto getById(Long id) {
        return tournamentRepository.findById(id)
                .map(tournamentMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Tournament " + id + " not found"));
    }

    public TournamentDto update(Long id, TournamentDto dto) {
        Tournament existing = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament " + id + " not found"));
        existing.setName(dto.getName());
        if (dto.getSportId() != null) {
            Sport sport = sportRepository.findById(dto.getSportId())
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Sport not found: " + dto.getSportId()));
            existing.setSport(sport);
        }
        return tournamentMapper.toDto(tournamentRepository.save(existing));
    }

    public void delete(Long id) {
        tournamentRepository.deleteById(id);
    }
}
