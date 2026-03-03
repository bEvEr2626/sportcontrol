package com.example.sportcontrol.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.mapper.SportMapper;
import com.example.sportcontrol.repository.SportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    public final List<SportDto> getAllSports() {
        return sportRepository.findAll().stream()
                .map(sportMapper::toDto)
                .toList();
    }

    public SportDto createSport(SportDto dto) { 
        Sport entity = sportMapper.toEntity(dto);
        Sport savedEntity = sportRepository.save(entity);
        return sportMapper.toDto(savedEntity);
    }
}
