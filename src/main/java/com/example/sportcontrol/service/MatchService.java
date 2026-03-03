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

    private final MatchRepository eventRepository;

    private final MatchMapper matchMapper;

    public final List<MatchDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(matchMapper::toDto)
                .toList();
    }

    public final MatchDto createEvent(final MatchDto dto) {
        Match entity = matchMapper.toEntity(dto);
        Match savedEntity = eventRepository.save(entity);
        return matchMapper.toDto(savedEntity);
    }

    public final void deleteEvent(final Long id) {
        eventRepository.deleteById(id);
    }

    public final MatchDto getById(final Long id) {
        return eventRepository.findById(id)
                .map(matchMapper::toDto)
                .orElse(null);
    }

    public final List<MatchDto> getEventsByLocation(final String location) {
        return eventRepository.findByLocation(location)
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }
}
