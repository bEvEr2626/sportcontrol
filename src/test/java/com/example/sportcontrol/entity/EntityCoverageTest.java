package com.example.sportcontrol.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class EntityCoverageTest {

    @Test
    void sportSupportsAccessorsCollectionsAndEquality() {
        Sport sport = new Sport();
        List<Tournament> tournaments = new ArrayList<>();
        Tournament tournament = new Tournament();
        tournaments.add(tournament);

        sport.setId(1L);
        sport.setName("Football");
        sport.setTournaments(tournaments);

        Sport same = new Sport();
        same.setId(1L);
        Sport different = new Sport();
        different.setId(2L);

        assertEquals(1L, sport.getId());
        assertEquals("Football", sport.getName());
        assertSame(tournaments, sport.getTournaments());
        assertEquals(sport, same);
        assertEquals(sport.hashCode(), same.hashCode());
        assertNotEquals(sport, different);
        assertNotEquals(sport, null);
        assertNotEquals(sport, "sport");
        assertNotNull(sport.toString());
    }

    @Test
    void teamSupportsAccessorsCollectionsAndEquality() {
        Team team = new Team();
        List<Player> players = new ArrayList<>();
        List<Tournament> tournaments = new ArrayList<>();
        List<Match> homeMatches = new ArrayList<>();
        List<Match> awayMatches = new ArrayList<>();

        team.setId(10L);
        team.setName("Spartak");
        team.setPlayers(players);
        team.setTournaments(tournaments);
        team.setHomeMatches(homeMatches);
        team.setAwayMatches(awayMatches);

        Team same = new Team();
        same.setId(10L);
        Team different = new Team();
        different.setId(11L);

        assertEquals(10L, team.getId());
        assertEquals("Spartak", team.getName());
        assertSame(players, team.getPlayers());
        assertSame(tournaments, team.getTournaments());
        assertSame(homeMatches, team.getHomeMatches());
        assertSame(awayMatches, team.getAwayMatches());
        assertEquals(team, same);
        assertEquals(team.hashCode(), same.hashCode());
        assertNotEquals(team, different);
        assertNotEquals(team, null);
        assertNotEquals(team, "team");
        assertNotNull(team.toString());
    }

    @Test
    void tournamentSupportsAccessorsCollectionsAndEquality() {
        Tournament tournament = new Tournament();
        Sport sport = new Sport();
        List<Match> matches = new ArrayList<>();
        List<Team> teams = new ArrayList<>();

        tournament.setId(20L);
        tournament.setName("Cup");
        tournament.setSport(sport);
        tournament.setMatches(matches);
        tournament.setTeams(teams);

        Tournament same = new Tournament();
        same.setId(20L);
        Tournament different = new Tournament();
        different.setId(21L);

        assertEquals(20L, tournament.getId());
        assertEquals("Cup", tournament.getName());
        assertSame(sport, tournament.getSport());
        assertSame(matches, tournament.getMatches());
        assertSame(teams, tournament.getTeams());
        assertEquals(tournament, same);
        assertEquals(tournament.hashCode(), same.hashCode());
        assertNotEquals(tournament, different);
        assertNotEquals(tournament, null);
        assertNotEquals(tournament, "tournament");
        assertNotNull(tournament.toString());
    }

    @Test
    void matchSupportsAccessorsAndEquality() {
        Match match = new Match();
        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        match.setId(30L);
        match.setName("Final");
        match.setLocation("Luzhniki");
        match.setDate(LocalDateTime.of(2026, 5, 1, 18, 0));
        match.setTournament(tournament);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);

        Match same = new Match();
        same.setId(30L);
        Match different = new Match();
        different.setId(31L);

        assertEquals(30L, match.getId());
        assertEquals("Final", match.getName());
        assertEquals("Luzhniki", match.getLocation());
        assertEquals(LocalDateTime.of(2026, 5, 1, 18, 0), match.getDate());
        assertSame(tournament, match.getTournament());
        assertSame(homeTeam, match.getHomeTeam());
        assertSame(awayTeam, match.getAwayTeam());
        assertEquals(match, same);
        assertEquals(match.hashCode(), same.hashCode());
        assertNotEquals(match, different);
        assertNotEquals(match, null);
        assertNotEquals(match, "match");
        assertNotNull(match.toString());
    }
}