package com.example.sportcontrol.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.exception.EntityNotFoundException;
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

    public final SportDto getById(Long id) {
        return sportRepository.findById(id)
                .map(sportMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Sport " + id + " not found"));
    }

    public SportDto create(SportDto dto) { 
        Sport entity = sportMapper.toEntity(dto);
        Sport savedEntity = sportRepository.save(entity);
        return sportMapper.toDto(savedEntity);
    }

    public SportDto update(Long id, SportDto dto) {
        Sport existing = sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sport not found: " + id));
        existing.setName(dto.getName());
        existing.setSlug(dto.getSlug());
        return sportMapper.toDto(sportRepository.save(existing));
    }

    public void delete(Long id) {
        sportRepository.deleteById(id);
    }
}
