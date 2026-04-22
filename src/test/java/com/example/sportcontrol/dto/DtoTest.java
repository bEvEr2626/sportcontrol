package com.example.sportcontrol.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void simpleLombokDtosSupportAccessorsAndEquality() {
        SportDto sportLeft = new SportDto();
        sportLeft.setId(1L);
        sportLeft.setName("Football");

        SportDto sportRight = new SportDto();
        sportRight.setId(1L);
        sportRight.setName("Football");

        assertEquals(sportLeft, sportRight);
        assertEquals(sportLeft.hashCode(), sportRight.hashCode());
        assertTrue(sportLeft.toString().contains("Football"));

        TeamDto teamLeft = new TeamDto();
        teamLeft.setId(10L);
        teamLeft.setName("Spartak");

        TeamDto teamRight = new TeamDto();
        teamRight.setId(10L);
        teamRight.setName("Spartak");

        assertEquals(teamLeft, teamRight);
        assertEquals(teamLeft.hashCode(), teamRight.hashCode());
        assertTrue(teamLeft.toString().contains("Spartak"));

        PlayerDto playerLeft = new PlayerDto();
        playerLeft.setId(100L);
        playerLeft.setName("John Smith");
        playerLeft.setTeamId(10L);

        PlayerDto playerRight = new PlayerDto();
        playerRight.setId(100L);
        playerRight.setName("John Smith");
        playerRight.setTeamId(10L);

        assertEquals(playerLeft, playerRight);
        assertEquals(playerLeft.hashCode(), playerRight.hashCode());
        assertTrue(playerLeft.toString().contains("John Smith"));

        TournamentDto tournamentLeft = new TournamentDto();
        tournamentLeft.setId(200L);
        tournamentLeft.setName("Russian Cup");
        tournamentLeft.setSportId(1L);

        TournamentDto tournamentRight = new TournamentDto();
        tournamentRight.setId(200L);
        tournamentRight.setName("Russian Cup");
        tournamentRight.setSportId(1L);

        assertEquals(tournamentLeft, tournamentRight);
        assertEquals(tournamentLeft.hashCode(), tournamentRight.hashCode());
        assertTrue(tournamentLeft.toString().contains("Russian Cup"));
    }

    @Test
    void asyncDtosSupportConstructorsAndAccessors() {
        AsyncTaskAcceptedResponseDto acceptedNoArgs = new AsyncTaskAcceptedResponseDto();
        acceptedNoArgs.setTaskId("task-1");
        assertEquals("task-1", acceptedNoArgs.getTaskId());

        AsyncTaskAcceptedResponseDto acceptedAllArgs = new AsyncTaskAcceptedResponseDto("task-2");
        assertEquals("task-2", acceptedAllArgs.getTaskId());

        LocalDateTime createdAt = LocalDateTime.of(2026, 4, 21, 15, 20, 0);
        LocalDateTime startedAt = LocalDateTime.of(2026, 4, 21, 15, 20, 1);
        LocalDateTime completedAt = LocalDateTime.of(2026, 4, 21, 15, 20, 5);

        AsyncTaskStatusDto statusAllArgs = new AsyncTaskStatusDto(
            "task-3",
            "DONE",
            createdAt,
            startedAt,
            completedAt,
            3,
            null
        );

        assertEquals("task-3", statusAllArgs.getTaskId());
        assertEquals("DONE", statusAllArgs.getStatus());
        assertEquals(createdAt, statusAllArgs.getCreatedAt());
        assertEquals(startedAt, statusAllArgs.getStartedAt());
        assertEquals(completedAt, statusAllArgs.getCompletedAt());
        assertEquals(3, statusAllArgs.getProcessedItems());

        AsyncTaskStatusDto statusNoArgs = new AsyncTaskStatusDto();
        statusNoArgs.setTaskId("task-4");
        statusNoArgs.setStatus("FAILED");
        statusNoArgs.setCreatedAt(createdAt);
        statusNoArgs.setStartedAt(startedAt);
        statusNoArgs.setCompletedAt(completedAt);
        statusNoArgs.setProcessedItems(1);
        statusNoArgs.setErrorMessage("boom");

        assertEquals("task-4", statusNoArgs.getTaskId());
        assertEquals("FAILED", statusNoArgs.getStatus());
        assertEquals(createdAt, statusNoArgs.getCreatedAt());
        assertEquals(startedAt, statusNoArgs.getStartedAt());
        assertEquals(completedAt, statusNoArgs.getCompletedAt());
        assertEquals(1, statusNoArgs.getProcessedItems());
        assertEquals("boom", statusNoArgs.getErrorMessage());
    }

    @Test
    void matchDtoSupportsConstructorsAndAccessors() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 1, 18, 0);

        MatchDto allArgs = new MatchDto(
            1L,
            "Cup Final",
            "Luzhniki Stadium",
            date,
            2L,
            "Russian Cup",
            10L,
            "Spartak",
            11L,
            "Zenit"
        );

        assertEquals(1L, allArgs.getId());
        assertEquals("Cup Final", allArgs.getName());
        assertEquals("Luzhniki Stadium", allArgs.getLocation());
        assertEquals(date, allArgs.getDate());
        assertEquals(2L, allArgs.getTournamentId());
        assertEquals("Russian Cup", allArgs.getTournamentName());
        assertEquals(10L, allArgs.getHomeTeamId());
        assertEquals("Spartak", allArgs.getHomeTeamName());
        assertEquals(11L, allArgs.getAwayTeamId());
        assertEquals("Zenit", allArgs.getAwayTeamName());

        MatchDto noArgs = new MatchDto();
        noArgs.setId(2L);
        noArgs.setName("Semi Final");
        noArgs.setLocation("Saint Petersburg");
        noArgs.setDate(date);
        noArgs.setTournamentId(3L);
        noArgs.setTournamentName("League");
        noArgs.setHomeTeamId(12L);
        noArgs.setHomeTeamName("CSKA");
        noArgs.setAwayTeamId(13L);
        noArgs.setAwayTeamName("Dynamo");

        assertEquals(2L, noArgs.getId());
        assertEquals("Semi Final", noArgs.getName());
        assertEquals("Saint Petersburg", noArgs.getLocation());
        assertEquals(date, noArgs.getDate());
        assertEquals(3L, noArgs.getTournamentId());
        assertEquals("League", noArgs.getTournamentName());
        assertEquals(12L, noArgs.getHomeTeamId());
        assertEquals("CSKA", noArgs.getHomeTeamName());
        assertEquals(13L, noArgs.getAwayTeamId());
        assertEquals("Dynamo", noArgs.getAwayTeamName());
    }

    @Test
    void raceConditionDemoDtoSupportsConstructorsAndAccessors() {
        RaceConditionDemoDto allArgs = new RaceConditionDemoDto(64, 10000, 640000, 523418, 640000, 640000, true);

        assertEquals(64, allArgs.getThreads());
        assertEquals(10000, allArgs.getIncrementsPerThread());
        assertEquals(640000, allArgs.getExpected());
        assertEquals(523418, allArgs.getUnsafeCounter());
        assertEquals(640000, allArgs.getSynchronizedCounter());
        assertEquals(640000, allArgs.getAtomicCounter());
        assertTrue(allArgs.isRaceConditionDetected());

        RaceConditionDemoDto noArgs = new RaceConditionDemoDto();
        noArgs.setThreads(8);
        noArgs.setIncrementsPerThread(100);
        noArgs.setExpected(800);
        noArgs.setUnsafeCounter(700);
        noArgs.setSynchronizedCounter(800);
        noArgs.setAtomicCounter(800);
        noArgs.setRaceConditionDetected(true);

        assertEquals(8, noArgs.getThreads());
        assertEquals(100, noArgs.getIncrementsPerThread());
        assertEquals(800, noArgs.getExpected());
        assertEquals(700, noArgs.getUnsafeCounter());
        assertEquals(800, noArgs.getSynchronizedCounter());
        assertEquals(800, noArgs.getAtomicCounter());
        assertTrue(noArgs.isRaceConditionDetected());
    }

    @Test
    void dtoValidationRejectsInvalidValues() {
        SportDto sportDto = new SportDto();
        sportDto.setName("A");
        assertTrue(violatedProperties(sportDto).contains("name"));

        TeamDto teamDto = new TeamDto();
        teamDto.setName("A");
        assertTrue(violatedProperties(teamDto).contains("name"));

        PlayerDto playerDto = new PlayerDto();
        playerDto.setName("A");
        assertTrue(violatedProperties(playerDto).contains("name"));
        assertTrue(violatedProperties(playerDto).contains("teamId"));

        TournamentDto tournamentDto = new TournamentDto();
        tournamentDto.setName("A");
        assertTrue(violatedProperties(tournamentDto).contains("name"));
        assertTrue(violatedProperties(tournamentDto).contains("sportId"));

        MatchDto matchDto = new MatchDto();
        matchDto.setName(" ");
        matchDto.setLocation(" ");
        Set<String> requiredViolations = violatedProperties(matchDto);
        assertTrue(requiredViolations.contains("name"));
        assertTrue(requiredViolations.contains("location"));
        assertTrue(requiredViolations.contains("date"));
        assertTrue(requiredViolations.contains("homeTeamId"));
        assertTrue(requiredViolations.contains("awayTeamId"));

        MatchDto longFieldsMatchDto = new MatchDto();
        longFieldsMatchDto.setName("a".repeat(256));
        longFieldsMatchDto.setLocation("b".repeat(256));
        longFieldsMatchDto.setDate(LocalDateTime.of(2026, 5, 1, 18, 0));
        longFieldsMatchDto.setHomeTeamId(10L);
        longFieldsMatchDto.setAwayTeamId(11L);
        Set<String> sizeViolations = violatedProperties(longFieldsMatchDto);
        assertTrue(sizeViolations.contains("name"));
        assertTrue(sizeViolations.contains("location"));
    }

    private <T> Set<String> violatedProperties(T dto) {
        return validator.validate(dto)
            .stream()
            .map(violation -> violation.getPropertyPath().toString())
            .collect(Collectors.toSet());
    }
}