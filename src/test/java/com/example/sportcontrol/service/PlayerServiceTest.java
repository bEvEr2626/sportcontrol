package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.entity.Player;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.mapper.PlayerMapper;
import com.example.sportcontrol.repository.PlayerRepository;
import com.example.sportcontrol.repository.TeamRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerService service;

    @Test
    void getAllPlayersReturnsMappedPage() {
        Player entity = new Player();
        PlayerDto dto = buildPlayerDto(1L, "John", 10L);
        Page<Player> page = new PageImpl<>(java.util.List.of(entity));

        when(playerRepository.findAllBy(PageRequest.of(0, 5))).thenReturn(page);
        when(playerMapper.toDto(entity)).thenReturn(dto);

        Page<PlayerDto> result = service.getAllPlayers(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().getFirst());
    }

    @Test
    void getByIdReturnsDtoWhenFound() {
        Player entity = new Player();
        PlayerDto dto = buildPlayerDto(2L, "Mike", 10L);

        when(playerRepository.findById(2L)).thenReturn(Optional.of(entity));
        when(playerMapper.toDto(entity)).thenReturn(dto);

        PlayerDto result = service.getById(2L);

        assertEquals(dto, result);
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(playerRepository.findById(7L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getById(7L));

        assertEquals("Player 7 not found", exception.getMessage());
    }

    @Test
    void createSavesPlayerWithTeamWhenNotDuplicate() {
        PlayerDto input = buildPlayerDto(null, "John", 10L);
        Player mapped = new Player();
        Player saved = new Player();
        saved.setId(3L);
        PlayerDto savedDto = buildPlayerDto(3L, "John", 10L);
        Team team = new Team();

        when(playerRepository.findByNameAndTeam_Id("John", 10L)).thenReturn(Optional.empty());
        when(playerMapper.toEntity(input)).thenReturn(mapped);
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(playerRepository.save(mapped)).thenReturn(saved);
        when(playerMapper.toDto(saved)).thenReturn(savedDto);

        PlayerDto result = service.create(input);

        assertEquals(savedDto, result);
        assertEquals(team, mapped.getTeam());
    }

    @Test
    void createThrowsWhenDuplicatePlayerExists() {
        PlayerDto input = buildPlayerDto(null, "John", 10L);
        Player existing = new Player();

        when(playerRepository.findByNameAndTeam_Id("John", 10L)).thenReturn(Optional.of(existing));

        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> service.create(input)
        );

        assertEquals("Player with this name already exists in the team", exception.getMessage());
        verify(playerRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createThrowsWhenTeamNotFound() {
        PlayerDto input = buildPlayerDto(null, "John", 10L);
        Player mapped = new Player();

        when(playerRepository.findByNameAndTeam_Id("John", 10L)).thenReturn(Optional.empty());
        when(playerMapper.toEntity(input)).thenReturn(mapped);
        when(teamRepository.findById(10L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.create(input));

        assertEquals("Team 10 not found", exception.getMessage());
        verify(playerRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateUpdatesFieldsAndTeam() {
        PlayerDto input = buildPlayerDto(null, "Updated", 20L);
        Player existing = new Player();
        Player saved = new Player();
        saved.setId(5L);
        PlayerDto savedDto = buildPlayerDto(5L, "Updated", 20L);
        Team team = new Team();

        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(teamRepository.findById(20L)).thenReturn(Optional.of(team));
        when(playerRepository.save(existing)).thenReturn(saved);
        when(playerMapper.toDto(saved)).thenReturn(savedDto);

        PlayerDto result = service.update(5L, input);

        assertEquals(savedDto, result);
        assertEquals("Updated", existing.getName());
        assertEquals(team, existing.getTeam());
    }

    @Test
    void updateThrowsWhenPlayerNotFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        PlayerDto input = buildPlayerDto(null, "Name", 10L);

        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.update(99L, input)
        );

        assertEquals("Player 99 not found", exception.getMessage());
    }

    @Test
    void deleteCallsRepositoryDeleteById() {
        service.delete(8L);

        verify(playerRepository).deleteById(8L);
    }

    private PlayerDto buildPlayerDto(Long id, String name, Long teamId) {
        PlayerDto dto = new PlayerDto();
        dto.setId(id);
        dto.setName(name);
        dto.setTeamId(teamId);
        return dto;
    }
}
