package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.mapper.TeamMapper;
import com.example.sportcontrol.repository.TeamRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamService service;

    @Test
    void getAllReturnsMappedList() {
        Team first = new Team();
        Team second = new Team();
        TeamDto firstDto = buildTeamDto(1L, "Spartak");
        TeamDto secondDto = buildTeamDto(2L, "Zenit");

        when(teamRepository.findAll()).thenReturn(List.of(first, second));
        when(teamMapper.toDto(any(Team.class))).thenReturn(firstDto, secondDto);

        List<TeamDto> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
    }

    @Test
    void getByIdReturnsMappedDto() {
        Team entity = new Team();
        TeamDto dto = buildTeamDto(3L, "Loko");

        when(teamRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(teamMapper.toDto(entity)).thenReturn(dto);

        TeamDto result = service.getById(3L);

        assertEquals(dto, result);
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getById(99L));

        assertEquals("Team 99 not found", exception.getMessage());
    }

    @Test
    void createSavesAndReturnsMappedDto() {
        TeamDto input = buildTeamDto(null, "Krasnodar");
        Team mapped = new Team();
        Team saved = new Team();
        saved.setId(4L);
        TeamDto savedDto = buildTeamDto(4L, "Krasnodar");

        when(teamMapper.toEntity(input)).thenReturn(mapped);
        when(teamRepository.save(mapped)).thenReturn(saved);
        when(teamMapper.toDto(saved)).thenReturn(savedDto);

        TeamDto result = service.create(input);

        assertEquals(savedDto, result);
    }

    @Test
    void createThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(null));

        assertEquals("Team payload cannot be null", exception.getMessage());
    }

    @Test
    void updateUpdatesNameAndReturnsDto() {
        TeamDto input = buildTeamDto(null, "Updated Team");
        Team existing = new Team();
        Team saved = new Team();
        saved.setId(5L);
        TeamDto savedDto = buildTeamDto(5L, "Updated Team");

        when(teamRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(teamRepository.save(existing)).thenReturn(saved);
        when(teamMapper.toDto(saved)).thenReturn(savedDto);

        TeamDto result = service.update(5L, input);

        assertEquals(savedDto, result);
        assertEquals("Updated Team", existing.getName());
    }

    @Test
    void updateThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(1L, null));

        assertEquals("Team payload cannot be null", exception.getMessage());
    }

    @Test
    void deleteCallsRepositoryDeleteById() {
        service.delete(6L);

        verify(teamRepository).deleteById(6L);
    }

    private TeamDto buildTeamDto(Long id, String name) {
        TeamDto dto = new TeamDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

}
