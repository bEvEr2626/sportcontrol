package com.example.sportcontrol.service;

import java.util.List;
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
        return sportRepository.findById(id)
            .map(sportMapper::toDto)
            .orElseThrow(() -> {
                LOG.warn("Sport not found: {}", id);
                return new NoSuchElementException("Sport " + id + " not found");
            });
    }

    public SportDto create(SportDto dto) { 
        LOG.info("Creating sport: {}", dto);
        Sport entity = sportMapper.toEntity(dto);
        Sport savedEntity = sportRepository.save(entity);
        LOG.info("Sport created with id={}", savedEntity.getId());
        return sportMapper.toDto(savedEntity);
    }

    public SportDto update(Long id, SportDto dto) {
        LOG.info("Updating sport id={} with data {}", id, dto);
        Sport existing = sportRepository.findById(id)
            .orElseThrow(() -> {
                LOG.warn("Sport not found for update: {}", id);
                return new NoSuchElementException("Sport not found: " + id);
            });
        existing.setName(dto.getName());
        Sport saved = sportRepository.save(existing);
        LOG.info("Sport updated with id={}", saved.getId());
        return sportMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting sport with id={}", id);
        sportRepository.deleteById(id);
        LOG.info("Sport deleted: {}", id);
    }
}
