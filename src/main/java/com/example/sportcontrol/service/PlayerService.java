package com.example.sportcontrol.service;

import org.springframework.stereotype.Service;
import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.entity.Player;
import com.example.sportcontrol.exception.EntityNotFoundException;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.mapper.PlayerMapper;
import com.example.sportcontrol.repository.PlayerRepository;
import com.example.sportcontrol.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final String NOTFOUND = " not found";

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerDto> getAllPlayers() {
        return playerRepository.findAllBy().stream()
                .map(playerMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> getAllPlayersNPlusOne() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toDto)
                .toList();
    }

    public PlayerDto getById(Long id) {
        return playerRepository.findById(id)
                .map(playerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Player " + id + NOTFOUND));
    }

    public PlayerDto create(PlayerDto dto) {
        Player entity = playerMapper.toEntity(dto);
        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team " + dto.getTeamId() + NOTFOUND));
            entity.setTeam(team);
        }
        return playerMapper.toDto(playerRepository.save(entity));
    }

    public PlayerDto update(Long id, PlayerDto dto) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player " + id + NOTFOUND));
        existing.setName(dto.getName());
        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + dto.getTeamId()));
            existing.setTeam(team);
        }
        return playerMapper.toDto(playerRepository.save(existing));
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
}
