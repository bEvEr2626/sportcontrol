package com.example.sportcontrol.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Sport;
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
        return tournamentRepository.findAll().stream()
                .map(tournamentMapper::toDto)
                .toList();
    }

    public final TournamentDto createTournament(TournamentDto dto) {
        Tournament entity = tournamentMapper.toEntity(dto);
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new RuntimeException("Sport not found with id: " + dto.getSportId()));
        
        entity.setSport(sport);
        Tournament savedEntity = tournamentRepository.save(entity);
        return tournamentMapper.toDto(savedEntity);
    }
}
