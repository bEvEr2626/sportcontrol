package com.example.sportcontrol.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.entity.Sport;
import java.util.NoSuchElementException;
import com.example.sportcontrol.mapper.SportMapper;
import com.example.sportcontrol.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SportService {

    private static final Logger LOG = LoggerFactory.getLogger(SportService.class);
    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    public final List<SportDto> getAllSports() {
        LOG.info("Getting all sports");
        return sportRepository.findAll().stream()
                .map(sportMapper::toDto)
                .toList();
    }

    public final SportDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return sportMapper.toDto(findSportById(id));
    }

    public SportDto create(SportDto dto) { 
        SportDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Sport payload cannot be null"));
        LOG.info("Creating sport: {}", safeDto);
        Sport entity = sportMapper.toEntity(safeDto);
        Sport savedEntity = sportRepository.save(entity);
        LOG.info("Sport created with id={}", savedEntity.getId());
        return sportMapper.toDto(savedEntity);
    }

    public SportDto update(Long id, SportDto dto) {
        SportDto safeDto = Optional.ofNullable(dto)
            .orElseThrow(() -> new IllegalArgumentException("Sport payload cannot be null"));
        LOG.info("Updating sport id={} with data {}", id, safeDto);
        Sport existing = findSportById(id);
        existing.setName(safeDto.getName());
        Sport saved = sportRepository.save(existing);
        LOG.info("Sport updated with id={}", saved.getId());
        return sportMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting sport with id={}", id);
        sportRepository.deleteById(id);
        LOG.info("Sport deleted: {}", id);
    }

    private Sport findSportById(Long id) {
        return sportRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Sport not found: {}", id);
                return new NoSuchElementException("Sport " + id + " not found");
            });
    }
}
