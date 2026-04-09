package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.mapper.TournamentMapper;
import com.example.sportcontrol.repository.SportRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private SportRepository sportRepository;

    @Mock
    private TournamentMapper tournamentMapper;

    @InjectMocks
    private TournamentService service;

    @Test
    void getAllTournamentsReturnsMappedList() {
        Tournament first = new Tournament();
        Tournament second = new Tournament();
        TournamentDto firstDto = buildTournamentDto(1L, "Champions Cup", 10L);
        TournamentDto secondDto = buildTournamentDto(2L, "League", 11L);

        when(tournamentRepository.findAllBy()).thenReturn(List.of(first, second));
        when(tournamentMapper.toDto(any(Tournament.class))).thenReturn(firstDto, secondDto);

        List<TournamentDto> result = service.getAllTournaments();

        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
    }

    @Test
    void createSavesTournamentWithSport() {
        TournamentDto input = buildTournamentDto(null, "Cup", 10L);
        Tournament mapped = new Tournament();
        Tournament saved = new Tournament();
        saved.setId(3L);
        TournamentDto savedDto = buildTournamentDto(3L, "Cup", 10L);
        Sport sport = new Sport();

        when(tournamentMapper.toEntity(input)).thenReturn(mapped);
        when(sportRepository.findById(10L)).thenReturn(Optional.of(sport));
        when(tournamentRepository.save(mapped)).thenReturn(saved);
        when(tournamentMapper.toDto(saved)).thenReturn(savedDto);

        TournamentDto result = service.create(input);

        assertEquals(savedDto, result);
        assertEquals(sport, mapped.getSport());
    }

    @Test
    void createThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(null));

        assertEquals("Tournament payload cannot be null", exception.getMessage());
    }

    @Test
    void createThrowsWhenSportIdIsNull() {
        TournamentDto input = buildTournamentDto(null, "Cup", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(input));

        assertEquals("Sport id is required for tournament creation", exception.getMessage());
        verify(tournamentRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createThrowsWhenSportNotFound() {
        TournamentDto input = buildTournamentDto(null, "Cup", 10L);
        Tournament mapped = new Tournament();

        when(tournamentMapper.toEntity(input)).thenReturn(mapped);
        when(sportRepository.findById(10L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.create(input));

        assertEquals("Sport not found: 10", exception.getMessage());
        verify(tournamentRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void getByIdReturnsMappedDto() {
        Tournament entity = new Tournament();
        TournamentDto dto = buildTournamentDto(8L, "National Cup", 5L);

        when(tournamentRepository.findById(8L)).thenReturn(Optional.of(entity));
        when(tournamentMapper.toDto(entity)).thenReturn(dto);

        TournamentDto result = service.getById(8L);

        assertEquals(dto, result);
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(tournamentRepository.findById(8L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getById(8L));

        assertEquals("Tournament 8 not found", exception.getMessage());
    }

    @Test
    void updateUpdatesNameAndSportWhenProvided() {
        TournamentDto input = buildTournamentDto(null, "Updated Cup", 11L);
        Tournament existing = new Tournament();
        Tournament saved = new Tournament();
        saved.setId(12L);
        TournamentDto savedDto = buildTournamentDto(12L, "Updated Cup", 11L);
        Sport sport = new Sport();

        when(tournamentRepository.findById(12L)).thenReturn(Optional.of(existing));
        when(sportRepository.findById(11L)).thenReturn(Optional.of(sport));
        when(tournamentRepository.save(existing)).thenReturn(saved);
        when(tournamentMapper.toDto(saved)).thenReturn(savedDto);

        TournamentDto result = service.update(12L, input);

        assertEquals(savedDto, result);
        assertEquals("Updated Cup", existing.getName());
        assertEquals(sport, existing.getSport());
    }

    @Test
    void updateThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(1L, null));

        assertEquals("Tournament payload cannot be null", exception.getMessage());
    }

    @Test
    void deleteCallsRepositoryDeleteById() {
        service.delete(7L);

        verify(tournamentRepository).deleteById(7L);
    }

    private TournamentDto buildTournamentDto(Long id, String name, Long sportId) {
        TournamentDto dto = new TournamentDto();
        dto.setId(id);
        dto.setName(name);
        dto.setSportId(sportId);
        return dto;
    }

}
