package com.example.sportcontrol.service;

import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.entity.Player;
import java.util.NoSuchElementException;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.mapper.PlayerMapper;
import com.example.sportcontrol.repository.PlayerRepository;
import com.example.sportcontrol.repository.TeamRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);
    private static final String NOTFOUND = " not found";

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Transactional(readOnly = true)
    public Page<PlayerDto> getAllPlayers(Pageable pageable) {
        LOG.info("Getting all players, page: {}", pageable);
        return playerRepository.findAllBy(pageable)
                .map(playerMapper::toDto);
    }

    public PlayerDto getById(Long id) {
        LOG.debug("getById called with id={}", id);
        return playerRepository.findById(id)
            .map(playerMapper::toDto)
            .orElseThrow(() -> {
                LOG.warn("Player not found: {}", id);
                return new NoSuchElementException("Player " + id + NOTFOUND);
            });
    }

    public PlayerDto create(PlayerDto dto) {
        LOG.info("Creating player: {}", dto);
        Optional.ofNullable(dto.getTeamId())
            .flatMap(teamId -> Optional.ofNullable(dto.getName())
                .map(name -> playerRepository.findByNameAndTeam_Id(name, teamId)))
            .ifPresent(existing -> {
                LOG.warn("Player with name '{}' already exists in team {}", dto.getName(), dto.getTeamId());
                throw new org.springframework.dao.DataIntegrityViolationException("Player with this name already exists in the team");
            });
        Player entity = playerMapper.toEntity(dto);
        Optional.ofNullable(dto.getTeamId())
            .map(this::findTeamById)
            .ifPresent(entity::setTeam);
        Player saved = playerRepository.save(entity);
        LOG.info("Player created with id={}", saved.getId());
        return playerMapper.toDto(saved);
    }

    public PlayerDto update(Long id, PlayerDto dto) {
        LOG.info("Updating player id={} with data {}", id, dto);
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn("Player not found for update: {}", id);
                    return new NoSuchElementException("Player " + id + NOTFOUND);
                });
        existing.setName(dto.getName());
        Optional.ofNullable(dto.getTeamId())
            .map(this::findTeamById)
            .ifPresent(existing::setTeam);
        Player saved = playerRepository.save(existing);
        LOG.info("Player updated with id={}", saved.getId());
        return playerMapper.toDto(saved);
    }

    public void delete(Long id) {
        LOG.info("Deleting player with id={}", id);
        playerRepository.deleteById(id);
        LOG.info("Player deleted: {}", id);
    }

    private Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> {
                LOG.warn("Team not found: {}", teamId);
                return new NoSuchElementException("Team " + teamId + NOTFOUND);
            });
    }
}
