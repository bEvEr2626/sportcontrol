package com.example.sportcontrol.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.AsyncTaskAcceptedResponseDto;
import com.example.sportcontrol.dto.AsyncTaskStatusDto;
import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.service.MatchAsyncTaskService;
import com.example.sportcontrol.service.MatchService;
import com.example.sportcontrol.service.PlayerService;
import com.example.sportcontrol.service.SportService;
import com.example.sportcontrol.service.TeamService;
import com.example.sportcontrol.service.TournamentService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ControllerDelegationTest {

    @Mock
    private SportService sportService;
    @Mock
    private TeamService teamService;
    @Mock
    private TournamentService tournamentService;
    @Mock
    private PlayerService playerService;
    @Mock
    private MatchService matchService;
    @Mock
    private MatchAsyncTaskService matchAsyncTaskService;

    @InjectMocks
    private SportController sportController;
    @InjectMocks
    private TeamController teamController;
    @InjectMocks
    private TournamentController tournamentController;
    @InjectMocks
    private PlayerController playerController;
    @InjectMocks
    private MatchController matchController;

    @Test
    void sportControllerDelegatesAllOperations() {
        SportDto dto = new SportDto();
        dto.setId(1L);
        dto.setName("Football");

        when(sportService.getAllSports()).thenReturn(List.of(dto));
        when(sportService.create(dto)).thenReturn(dto);
        when(sportService.getById(1L)).thenReturn(dto);
        when(sportService.update(1L, dto)).thenReturn(dto);

        assertEquals(1, sportController.getAll().size());
        assertSame(dto, sportController.create(dto));
        assertSame(dto, sportController.getById(1L));
        assertSame(dto, sportController.update(1L, dto));
        assertSame(dto, sportController.patch(1L, dto));

        sportController.delete(1L);

        verify(sportService).getAllSports();
        verify(sportService).create(dto);
        verify(sportService).getById(1L);
        verify(sportService, times(2)).update(1L, dto);
        verify(sportService).delete(1L);
    }

    @Test
    void teamControllerDelegatesAllOperations() {
        TeamDto dto = new TeamDto();
        dto.setId(10L);
        dto.setName("Spartak");

        when(teamService.getAll()).thenReturn(List.of(dto));
        when(teamService.create(dto)).thenReturn(dto);
        when(teamService.getById(10L)).thenReturn(dto);
        when(teamService.update(10L, dto)).thenReturn(dto);

        assertEquals(1, teamController.getAll().size());
        assertSame(dto, teamController.create(dto));
        assertSame(dto, teamController.getById(10L));
        assertSame(dto, teamController.update(10L, dto));
        assertSame(dto, teamController.patch(10L, dto));

        teamController.delete(10L);

        verify(teamService).getAll();
        verify(teamService).create(dto);
        verify(teamService).getById(10L);
        verify(teamService, times(2)).update(10L, dto);
        verify(teamService).delete(10L);
    }

    @Test
    void tournamentControllerDelegatesAllOperations() {
        TournamentDto dto = new TournamentDto();
        dto.setId(5L);
        dto.setName("Cup");
        dto.setSportId(1L);

        when(tournamentService.getAllTournaments()).thenReturn(List.of(dto));
        when(tournamentService.create(dto)).thenReturn(dto);
        when(tournamentService.getById(5L)).thenReturn(dto);
        when(tournamentService.update(5L, dto)).thenReturn(dto);

        assertEquals(1, tournamentController.getAll().size());
        assertSame(dto, tournamentController.create(dto));
        assertSame(dto, tournamentController.getById(5L));
        assertSame(dto, tournamentController.update(5L, dto));
        assertSame(dto, tournamentController.patch(5L, dto));

        tournamentController.delete(5L);

        verify(tournamentService).getAllTournaments();
        verify(tournamentService).create(dto);
        verify(tournamentService).getById(5L);
        verify(tournamentService, times(2)).update(5L, dto);
        verify(tournamentService).delete(5L);
    }

    @Test
    void playerControllerDelegatesAllOperations() {
        PlayerDto dto = new PlayerDto();
        dto.setId(11L);
        dto.setName("John");
        dto.setTeamId(10L);
        Page<PlayerDto> page = new PageImpl<>(List.of(dto), PageRequest.of(2, 5), 1);

        when(playerService.getAllPlayers(PageRequest.of(2, 5))).thenReturn(page);
        when(playerService.create(dto)).thenReturn(dto);
        when(playerService.getById(11L)).thenReturn(dto);
        when(playerService.update(11L, dto)).thenReturn(dto);

        assertSame(page, playerController.getAll(2, 5));
        assertSame(dto, playerController.create(dto));
        assertSame(dto, playerController.getById(11L));
        assertSame(dto, playerController.update(11L, dto));
        assertSame(dto, playerController.patch(11L, dto));

        playerController.delete(11L);

        verify(playerService).getAllPlayers(PageRequest.of(2, 5));
        verify(playerService).create(dto);
        verify(playerService).getById(11L);
        verify(playerService, times(2)).update(11L, dto);
        verify(playerService).delete(11L);
    }

    @Test
    void matchControllerDelegatesCrudAndSearch() {
        MatchDto dto = new MatchDto();
        dto.setId(15L);
        dto.setName("Final");
        Page<MatchDto> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        List<MatchDto> bulk = List.of(dto);
        AsyncTaskAcceptedResponseDto acceptedResponse = new AsyncTaskAcceptedResponseDto("task-1");
        AsyncTaskStatusDto statusResponse = new AsyncTaskStatusDto(
            "task-1",
            "RUNNING",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            null,
            null
        );

        when(matchService.findMatches(new MatchFilter(), 1, 20)).thenReturn(page);
        when(matchService.getById(15L)).thenReturn(dto);
        when(matchService.create(dto)).thenReturn(dto);
        when(matchService.update(15L, dto)).thenReturn(dto);
        when(matchService.patch(15L, dto)).thenReturn(dto);
        when(matchService.findMatchesNative(new MatchFilter(), 2, 3)).thenReturn(page);
        when(matchService.findMatches(new MatchFilter(), 2, 3)).thenReturn(page);
        when(matchService.bulkCreateNoTransactional(bulk)).thenReturn(bulk);
        when(matchService.bulkCreateTransactional(bulk)).thenReturn(bulk);
        when(matchAsyncTaskService.startBulkCreateTask(bulk)).thenReturn(acceptedResponse);
        when(matchAsyncTaskService.getTaskStatus("task-1")).thenReturn(statusResponse);

        assertSame(page, matchController.getAll(1, 20));
        assertSame(dto, matchController.getById(15L));
        assertSame(dto, matchController.create(dto));
        assertSame(dto, matchController.update(15L, dto));
        assertSame(dto, matchController.patch(15L, dto));

        ResponseEntity<Page<MatchDto>> nativeResult = matchController.search(new MatchFilter(), "native", PageRequest.of(2, 3));
        ResponseEntity<Page<MatchDto>> jpqlResult = matchController.search(new MatchFilter(), "jpql", PageRequest.of(2, 3));
        ResponseEntity<List<MatchDto>> bulkResult = matchController.bulkCreate(bulk);
        ResponseEntity<List<MatchDto>> bulkTxResult = matchController.bulkCreateTransactional(bulk);
        ResponseEntity<AsyncTaskAcceptedResponseDto> asyncAcceptedResult = matchController.bulkCreateAsync(bulk);
        ResponseEntity<AsyncTaskStatusDto> statusResult = matchController.getTaskStatus("task-1");

        assertSame(page, nativeResult.getBody());
        assertSame(page, jpqlResult.getBody());
        assertSame(bulk, bulkResult.getBody());
        assertSame(bulk, bulkTxResult.getBody());
        assertSame(acceptedResponse, asyncAcceptedResult.getBody());
        assertSame(statusResponse, statusResult.getBody());

        matchController.delete(15L);

        verify(matchService).findMatches(new MatchFilter(), 1, 20);
        verify(matchService).getById(15L);
        verify(matchService).create(dto);
        verify(matchService).update(15L, dto);
        verify(matchService).patch(15L, dto);
        verify(matchService).findMatchesNative(new MatchFilter(), 2, 3);
        verify(matchService).findMatches(new MatchFilter(), 2, 3);
        verify(matchService).bulkCreateNoTransactional(bulk);
        verify(matchService).bulkCreateTransactional(bulk);
        verify(matchAsyncTaskService).startBulkCreateTask(bulk);
        verify(matchAsyncTaskService).getTaskStatus("task-1");
        verify(matchService).delete(15L);
    }
}
