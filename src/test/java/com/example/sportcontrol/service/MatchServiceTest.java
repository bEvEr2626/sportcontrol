package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import com.example.sportcontrol.exception.BulkOperationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.repository.MatchRepository;
import com.example.sportcontrol.repository.TeamRepository;
import com.example.sportcontrol.repository.TournamentRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private MatchService service;

    private MatchFilter filter;

    @BeforeEach
    void setUp() {
        filter = new MatchFilter();
        filter.setName("Cup Final");
        lenient().when(validator.validate(any(MatchDto.class))).thenReturn(Collections.emptySet());
    }

    @Test
    void findMatchesReturnsMappedPageAndCachesResult() {
        Match entity = new Match();
        MatchDto dto = buildMatchDto(1L);
        Page<Match> entityPage = new PageImpl<>(List.of(entity));

        when(matchRepository.findWithFilters(filter, PageRequest.of(0, 10))).thenReturn(entityPage);
        when(matchMapper.toDto(entity)).thenReturn(dto);

        Page<MatchDto> firstCall = service.findMatches(filter, 0, 10);
        Page<MatchDto> secondCall = service.findMatches(filter, 0, 10);

        assertEquals(1, firstCall.getTotalElements());
        assertEquals(dto, firstCall.getContent().getFirst());
        assertEquals(firstCall, secondCall);
        verify(matchRepository, times(1)).findWithFilters(filter, PageRequest.of(0, 10));
        verify(matchMapper, times(1)).toDto(entity);
    }

    @Test
    void findMatchesNativeReturnsMappedPageAndCachesResult() {
        Match entity = new Match();
        MatchDto dto = buildMatchDto(1L);
        Page<Match> entityPage = new PageImpl<>(List.of(entity));

        when(matchRepository.findWithFiltersNative(filter, PageRequest.of(1, 5))).thenReturn(entityPage);
        when(matchMapper.toDto(entity)).thenReturn(dto);

        Page<MatchDto> firstCall = service.findMatchesNative(filter, 1, 5);
        Page<MatchDto> secondCall = service.findMatchesNative(filter, 1, 5);

        assertEquals(1, firstCall.getTotalElements());
        assertEquals(dto, firstCall.getContent().getFirst());
        assertEquals(firstCall, secondCall);
        verify(matchRepository, times(1)).findWithFiltersNative(filter, PageRequest.of(1, 5));
        verify(matchMapper, times(1)).toDto(entity);
    }

    @Test
    void getByIdReturnsDtoWhenFound() {
        Match entity = new Match();
        MatchDto dto = buildMatchDto(10L);

        when(matchRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(matchMapper.toDto(entity)).thenReturn(dto);

        MatchDto result = service.getById(10L);

        assertEquals(dto, result);
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(matchRepository.findById(100L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getById(100L));

        assertEquals("Match not found: 100", exception.getMessage());
    }

    @Test
    void createCreatesMatchWithRelations() {
        MatchDto input = buildMatchDto(null);
        Match mapped = new Match();
        Match saved = new Match();
        saved.setId(7L);
        MatchDto savedDto = buildMatchDto(7L);
        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchMapper.toEntity(input)).thenReturn(mapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(mapped)).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(savedDto);

        MatchDto result = service.create(input);

        assertEquals(savedDto, result);
        assertEquals(tournament, mapped.getTournament());
        assertEquals(homeTeam, mapped.getHomeTeam());
        assertEquals(awayTeam, mapped.getAwayTeam());
        verify(matchRepository).save(mapped);
    }

    @Test
    void createThrowsWhenTournamentNotFound() {
        MatchDto input = buildMatchDto(null);
        Match mapped = new Match();

        when(matchMapper.toEntity(input)).thenReturn(mapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.create(input));

        assertEquals("Tournament not found: 2", exception.getMessage());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createThrowsWhenAwayTeamNotFound() {
        MatchDto input = buildMatchDto(null);
        Match mapped = new Match();
        Tournament tournament = new Tournament();
        Team homeTeam = new Team();

        when(matchMapper.toEntity(input)).thenReturn(mapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.create(input));

        assertEquals("Team not found: 4", exception.getMessage());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void updateUpdatesExistingMatchAndRelations() {
        MatchDto input = buildMatchDto(null);
        Match existing = new Match();
        Match saved = new Match();
        saved.setId(10L);
        MatchDto savedDto = buildMatchDto(10L);
        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(existing)).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(savedDto);

        MatchDto result = service.update(10L, input);

        assertEquals(savedDto, result);
        assertEquals(input.getName(), existing.getName());
        assertEquals(input.getLocation(), existing.getLocation());
        assertEquals(input.getDate(), existing.getDate());
        assertEquals(tournament, existing.getTournament());
        assertEquals(homeTeam, existing.getHomeTeam());
        assertEquals(awayTeam, existing.getAwayTeam());
    }

    @Test
    void updateThrowsWhenMatchNotFound() {
        when(matchRepository.findById(77L)).thenReturn(Optional.empty());
        MatchDto input = buildMatchDto(null);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.update(77L, input));

        assertEquals("Match not found: 77", exception.getMessage());
    }

    @Test
    void patchUpdatesOnlyProvidedFields() {
        Match existing = new Match();
        existing.setName("Old Name");
        existing.setLocation("Old Location");
        existing.setDate(LocalDateTime.of(2026, 1, 1, 12, 0));

        MatchDto patch = new MatchDto();
        patch.setName("New Name");
        patch.setHomeTeamId(3L);

        Team homeTeam = new Team();
        Match saved = new Match();
        saved.setId(8L);
        MatchDto savedDto = buildMatchDto(8L);

        when(matchRepository.findById(8L)).thenReturn(Optional.of(existing));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(matchRepository.save(existing)).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(savedDto);

        MatchDto result = service.patch(8L, patch);

        assertEquals(savedDto, result);
        assertEquals("New Name", existing.getName());
        assertEquals("Old Location", existing.getLocation());
        assertNotNull(existing.getDate());
        assertEquals(homeTeam, existing.getHomeTeam());
    }

    @Test
    void patchThrowsWhenMatchNotFound() {
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());
        MatchDto emptyPatch = new MatchDto();

        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.patch(999L, emptyPatch)
        );

        assertEquals("Match not found: 999", exception.getMessage());
    }

    @Test
    void patchUpdatesAwayTeamWhenProvided() {
        Match existing = new Match();
        MatchDto patch = new MatchDto();
        patch.setAwayTeamId(4L);

        Team awayTeam = new Team();
        Match saved = new Match();
        saved.setId(12L);
        MatchDto savedDto = buildMatchDto(12L);

        when(matchRepository.findById(12L)).thenReturn(Optional.of(existing));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(existing)).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(savedDto);

        MatchDto result = service.patch(12L, patch);

        assertEquals(savedDto, result);
        assertEquals(awayTeam, existing.getAwayTeam());
    }

    @Test
    void deleteCallsRepositoryDeleteById() {
        service.delete(11L);

        verify(matchRepository).deleteById(11L);
    }

    @Test
    void getAllMapsAllMatchesToDtos() {
        Match first = new Match();
        Match second = new Match();
        MatchDto firstDto = buildMatchDto(1L);
        MatchDto secondDto = buildMatchDto(2L);

        when(matchRepository.findAllBy()).thenReturn(List.of(first, second));
        when(matchMapper.toDto(any(Match.class))).thenReturn(firstDto, secondDto);

        List<MatchDto> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
    }

    @Test
    void bulkCreateTransactionalCreatesAllMatches() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto secondInput = buildMatchDto(null);
        secondInput.setName("Semi Final");

        Match firstMapped = new Match();
        Match secondMapped = new Match();
        Match firstSaved = new Match();
        Match secondSaved = new Match();
        firstSaved.setId(1L);
        secondSaved.setId(2L);

        MatchDto firstSavedDto = buildMatchDto(1L);
        MatchDto secondSavedDto = buildMatchDto(2L);

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchMapper.toEntity(firstInput)).thenReturn(firstMapped);
        when(matchMapper.toEntity(secondInput)).thenReturn(secondMapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(firstSaved, secondSaved);
        when(matchMapper.toDto(firstSaved)).thenReturn(firstSavedDto);
        when(matchMapper.toDto(secondSaved)).thenReturn(secondSavedDto);

        List<MatchDto> result = service.bulkCreateTransactional(List.of(firstInput, secondInput));

        assertEquals(2, result.size());
        assertEquals(firstSavedDto, result.get(0));
        assertEquals(secondSavedDto, result.get(1));
    }

    @Test
    void bulkCreateTransactionalThrowsWhenListIsEmpty() {
        List<MatchDto> emptyList = List.of();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.bulkCreateTransactional(emptyList)
        );

        assertEquals("Matches list cannot be empty", exception.getMessage());
    }

    @Test
    void bulkCreateTransactionalThrowsWhenAnyRecordIsInvalidAndSavesNothing() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto invalidInput = buildMatchDto(null);
        invalidInput.setName("");

        @SuppressWarnings("unchecked")
        ConstraintViolation<MatchDto> violation = (ConstraintViolation<MatchDto>) org.mockito.Mockito.mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(org.mockito.Mockito.mock(jakarta.validation.Path.class));
        when(violation.getMessage()).thenReturn("must not be blank");
        when(violation.getPropertyPath().toString()).thenReturn("name");

        when(validator.validate(firstInput)).thenReturn(Collections.emptySet());
        when(validator.validate(invalidInput)).thenReturn(Set.of(violation));

        List<MatchDto> matches = List.of(firstInput, invalidInput);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.bulkCreateTransactional(matches)
        );

        assertEquals(
            "Invalid matches in transactional bulk: {match_2=name: must not be blank}",
            exception.getMessage()
        );
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void bulkCreateTransactionalThrowsWhenAnyRecordIsNullAndSavesNothing() {
        MatchDto validInput = buildMatchDto(null);
        List<MatchDto> matches = Arrays.asList(validInput, null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.bulkCreateTransactional(matches)
        );

        assertEquals(
            "Invalid matches in transactional bulk: {match_2=Match payload must not be null}",
            exception.getMessage()
        );
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void bulkCreateNoTransactionalCreatesAllWhenNoErrors() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto secondInput = buildMatchDto(null);
        secondInput.setName("Third Place");

        Match firstMapped = new Match();
        Match secondMapped = new Match();
        Match firstSaved = new Match();
        Match secondSaved = new Match();
        firstSaved.setId(21L);
        secondSaved.setId(22L);

        MatchDto firstSavedDto = buildMatchDto(21L);
        MatchDto secondSavedDto = buildMatchDto(22L);

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchMapper.toEntity(firstInput)).thenReturn(firstMapped);
        when(matchMapper.toEntity(secondInput)).thenReturn(secondMapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(firstSaved, secondSaved);
        when(matchMapper.toDto(firstSaved)).thenReturn(firstSavedDto);
        when(matchMapper.toDto(secondSaved)).thenReturn(secondSavedDto);

        List<MatchDto> result = service.bulkCreateNoTransactional(List.of(firstInput, secondInput));

        assertEquals(2, result.size());
    }

    @Test
    void bulkCreateNoTransactionalThrowsSummaryWhenAnyRecordFails() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto secondInput = buildMatchDto(null);
        secondInput.setName("Broken Match");
        List<MatchDto> input = List.of(firstInput, secondInput);

        Match firstMapped = new Match();
        Match secondMapped = new Match();
        Match firstSaved = new Match();
        firstSaved.setId(33L);
        MatchDto firstSavedDto = buildMatchDto(33L);

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchMapper.toEntity(firstInput)).thenReturn(firstMapped);
        when(matchMapper.toEntity(secondInput)).thenReturn(secondMapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(firstSaved).thenThrow(new RuntimeException("db error"));
        when(matchMapper.toDto(firstSaved)).thenReturn(firstSavedDto);

        BulkOperationException exception = assertThrows(
            BulkOperationException.class,
            () -> service.bulkCreateNoTransactional(input)
        );

        assertEquals(
            "Some matches were not saved. successCount=1, failedCount=1, failedMatches={match_2=db error}",
            exception.getMessage()
        );
        assertEquals(1, exception.getSuccessCount());
        assertEquals("db error", exception.getFailedItems().get("match_2"));
    }

    @Test
    void bulkCreateNoTransactionalUsesExceptionClassNameWhenMessageIsNull() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto secondInput = buildMatchDto(null);
        secondInput.setName("Broken Match Without Message");
        List<MatchDto> input = List.of(firstInput, secondInput);

        Match firstMapped = new Match();
        Match secondMapped = new Match();
        Match firstSaved = new Match();
        firstSaved.setId(41L);
        MatchDto firstSavedDto = buildMatchDto(41L);

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        when(matchMapper.toEntity(firstInput)).thenReturn(firstMapped);
        when(matchMapper.toEntity(secondInput)).thenReturn(secondMapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(firstSaved).thenThrow(new RuntimeException());
        when(matchMapper.toDto(firstSaved)).thenReturn(firstSavedDto);

        BulkOperationException exception = assertThrows(
            BulkOperationException.class,
            () -> service.bulkCreateNoTransactional(input)
        );

        assertEquals(
            "Some matches were not saved. successCount=1, failedCount=1, failedMatches={match_2=RuntimeException}",
            exception.getMessage()
        );
        assertEquals(1, exception.getSuccessCount());
        assertEquals("RuntimeException", exception.getFailedItems().get("match_2"));
    }

    @Test
    void bulkCreateNoTransactionalReportsBeanValidationErrorsAndSavesValidRows() {
        MatchDto firstInput = buildMatchDto(null);
        MatchDto invalidInput = buildMatchDto(null);
        invalidInput.setName("");
        List<MatchDto> input = List.of(firstInput, invalidInput);

        Match mapped = new Match();
        Match saved = new Match();
        saved.setId(51L);
        MatchDto savedDto = buildMatchDto(51L);

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        @SuppressWarnings("unchecked")
        ConstraintViolation<MatchDto> violation = (ConstraintViolation<MatchDto>) org.mockito.Mockito.mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(org.mockito.Mockito.mock(jakarta.validation.Path.class));
        when(violation.getMessage()).thenReturn("must not be blank");
        when(violation.getPropertyPath().toString()).thenReturn("name");

        when(validator.validate(firstInput)).thenReturn(Collections.emptySet());
        when(validator.validate(invalidInput)).thenReturn(Set.of(violation));

        when(matchMapper.toEntity(firstInput)).thenReturn(mapped);
        when(tournamentRepository.findById(2L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(4L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(savedDto);

        BulkOperationException exception = assertThrows(
            BulkOperationException.class,
            () -> service.bulkCreateNoTransactional(input)
        );

        assertEquals(1, exception.getSuccessCount());
        assertEquals("name: must not be blank", exception.getFailedItems().get("match_2"));
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void bulkCreateNoTransactionalThrowsWhenListIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.bulkCreateNoTransactional(null)
        );

        assertEquals("Matches list cannot be empty", exception.getMessage());
    }

    @Test
    void bulkCreateNoTransactionalThrowsWhenListIsEmpty() {
        List<MatchDto> emptyList = List.of();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.bulkCreateNoTransactional(emptyList)
        );

        assertEquals("Matches list cannot be empty", exception.getMessage());
    }

    private MatchDto buildMatchDto(Long id) {
        return new MatchDto(
            id,
            "Cup Final",
            "Stadium",
            LocalDateTime.of(2026, 5, 1, 18, 0),
            2L,
            "Premier Cup",
            3L,
            "Home Team",
            4L,
            "Away Team"
        );
    }
}
