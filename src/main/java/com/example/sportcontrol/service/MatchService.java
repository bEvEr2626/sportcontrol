package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.repository.MatchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;

    public final List<MatchDto> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    public final MatchDto createMatch(final MatchDto dto) {
        Match entity = matchMapper.toEntity(dto);
        Match savedEntity = matchRepository.save(entity);
        return matchMapper.toDto(savedEntity);
    }

    public final void deleteMatch(final Long id) {
        matchRepository.deleteById(id);
    }

    public final MatchDto getById(final Long id) {
        return matchRepository.findById(id)
                .map(matchMapper::toDto)
                .orElse(null);
    }

    public final List<MatchDto> getMatchesByLocation(final String location) {
        return matchRepository.findByLocation(location)
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }
}
