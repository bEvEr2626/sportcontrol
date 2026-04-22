package com.example.sportcontrol.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.sportcontrol.dto.MatchDto;
import com.example.sportcontrol.dto.PlayerDto;
import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.dto.TeamDto;
import com.example.sportcontrol.dto.TournamentDto;
import com.example.sportcontrol.entity.Match;
import com.example.sportcontrol.entity.Player;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.entity.Team;
import com.example.sportcontrol.entity.Tournament;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapperTest {

    private final MatchMapper matchMapper = Mappers.getMapper(MatchMapper.class);
    private final TeamMapper teamMapper = Mappers.getMapper(TeamMapper.class);
    private final PlayerMapper playerMapper = Mappers.getMapper(PlayerMapper.class);
    private final TournamentMapper tournamentMapper = Mappers.getMapper(TournamentMapper.class);
    private final SportMapper sportMapper = Mappers.getMapper(SportMapper.class);

    @Test
    void matchMapperMapsBothDirectionsAndNull() {
        Match entity = new Match();
        entity.setId(1L);
        entity.setName("Final");
        entity.setLocation("Moscow");
        entity.setDate(LocalDateTime.of(2026, 5, 1, 18, 0));

        Tournament tournament = new Tournament();
        tournament.setId(20L);
        tournament.setName("Cup");
        entity.setTournament(tournament);

        Team home = new Team();
        home.setId(30L);
        home.setName("Spartak");
        entity.setHomeTeam(home);

        Team away = new Team();
        away.setId(31L);
        away.setName("Zenit");
        entity.setAwayTeam(away);

        MatchDto dto = matchMapper.toDto(entity);
        assertEquals(1L, dto.getId());
        assertEquals("Final", dto.getName());
        assertEquals("Moscow", dto.getLocation());
        assertEquals(entity.getDate(), dto.getDate());
        assertEquals(20L, dto.getTournamentId());
        assertEquals("Cup", dto.getTournamentName());
        assertEquals(30L, dto.getHomeTeamId());
        assertEquals("Spartak", dto.getHomeTeamName());
        assertEquals(31L, dto.getAwayTeamId());
        assertEquals("Zenit", dto.getAwayTeamName());

        MatchDto source = new MatchDto();
        source.setId(999L);
        source.setName("Semi final");
        source.setLocation("SPB");
        source.setDate(LocalDateTime.of(2026, 6, 1, 20, 0));
        source.setTournamentId(11L);
        source.setHomeTeamId(21L);
        source.setAwayTeamId(22L);

        Match mappedEntity = matchMapper.toEntity(source);
        assertNull(mappedEntity.getId());
        assertEquals("Semi final", mappedEntity.getName());
        assertEquals("SPB", mappedEntity.getLocation());
        assertEquals(source.getDate(), mappedEntity.getDate());
        assertNull(mappedEntity.getTournament());
        assertNull(mappedEntity.getHomeTeam());
        assertNull(mappedEntity.getAwayTeam());

        assertNull(matchMapper.toDto(null));
        assertNull(matchMapper.toEntity(null));
    }

    @Test
    void teamMapperMapsBothDirectionsAndNull() {
        Team team = new Team();
        team.setId(5L);
        team.setName("Team A");

        TeamDto dto = teamMapper.toDto(team);
        assertEquals(5L, dto.getId());
        assertEquals("Team A", dto.getName());

        TeamDto source = new TeamDto();
        source.setId(66L);
        source.setName("Team B");
        Team entity = teamMapper.toEntity(source);
        assertNull(entity.getId());
        assertEquals("Team B", entity.getName());
        assertNotNull(entity.getPlayers());
        assertNotNull(entity.getTournaments());
        assertNotNull(entity.getHomeMatches());
        assertNotNull(entity.getAwayMatches());

        assertNull(teamMapper.toDto(null));
        assertNull(teamMapper.toEntity(null));
    }

    @Test
    void playerMapperMapsBothDirectionsAndNull() {
        Team team = new Team();
        team.setId(42L);

        Player player = new Player();
        player.setId(7L);
        player.setName("John");
        player.setTeam(team);

        PlayerDto dto = playerMapper.toDto(player);
        assertEquals(7L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals(42L, dto.getTeamId());

        PlayerDto source = new PlayerDto();
        source.setId(88L);
        source.setName("Mike");
        source.setTeamId(100L);

        Player entity = playerMapper.toEntity(source);
        assertNull(entity.getId());
        assertEquals("Mike", entity.getName());
        assertNull(entity.getTeam());

        assertNull(playerMapper.toDto(null));
        assertNull(playerMapper.toEntity(null));
    }

    @Test
    void tournamentMapperMapsBothDirectionsAndNull() {
        Sport sport = new Sport();
        sport.setId(3L);

        Tournament tournament = new Tournament();
        tournament.setId(9L);
        tournament.setName("League");
        tournament.setSport(sport);

        TournamentDto dto = tournamentMapper.toDto(tournament);
        assertEquals(9L, dto.getId());
        assertEquals("League", dto.getName());
        assertEquals(3L, dto.getSportId());

        TournamentDto source = new TournamentDto();
        source.setId(55L);
        source.setName("Cup");
        source.setSportId(44L);

        Tournament entity = tournamentMapper.toEntity(source);
        assertNull(entity.getId());
        assertEquals("Cup", entity.getName());
        assertNull(entity.getSport());
        assertNotNull(entity.getMatches());
        assertNotNull(entity.getTeams());

        assertNull(tournamentMapper.toDto(null));
        assertNull(tournamentMapper.toEntity(null));
    }

    @Test
    void sportMapperMapsBothDirectionsAndNull() {
        Sport sport = new Sport();
        sport.setId(12L);
        sport.setName("Basketball");

        SportDto dto = sportMapper.toDto(sport);
        assertEquals(12L, dto.getId());
        assertEquals("Basketball", dto.getName());

        SportDto source = new SportDto();
        source.setId(77L);
        source.setName("Volleyball");

        Sport entity = sportMapper.toEntity(source);
        assertNull(entity.getId());
        assertEquals("Volleyball", entity.getName());
        assertNotNull(entity.getTournaments());

        assertNull(sportMapper.toDto(null));
        assertNull(sportMapper.toEntity(null));
    }

    @Test
    void matchMapperHandlesMissingNestedRelationsAndNestedIds() {
        Match withoutRelations = new Match();
        withoutRelations.setId(101L);
        withoutRelations.setName("No relations");
        withoutRelations.setLocation("Nowhere");
        withoutRelations.setDate(LocalDateTime.of(2026, 1, 1, 10, 0));

        MatchDto dtoWithoutRelations = matchMapper.toDto(withoutRelations);
        assertEquals(101L, dtoWithoutRelations.getId());
        assertEquals("No relations", dtoWithoutRelations.getName());
        assertEquals("Nowhere", dtoWithoutRelations.getLocation());
        assertEquals(withoutRelations.getDate(), dtoWithoutRelations.getDate());
        assertNull(dtoWithoutRelations.getTournamentId());
        assertNull(dtoWithoutRelations.getTournamentName());
        assertNull(dtoWithoutRelations.getHomeTeamId());
        assertNull(dtoWithoutRelations.getHomeTeamName());
        assertNull(dtoWithoutRelations.getAwayTeamId());
        assertNull(dtoWithoutRelations.getAwayTeamName());

        Match withRelationsWithoutFields = new Match();
        withRelationsWithoutFields.setName("Partial relations");
        withRelationsWithoutFields.setLocation("Somewhere");
        withRelationsWithoutFields.setDate(LocalDateTime.of(2026, 1, 2, 11, 0));

        Tournament tournament = new Tournament();
        Team homeTeam = new Team();
        Team awayTeam = new Team();

        withRelationsWithoutFields.setTournament(tournament);
        withRelationsWithoutFields.setHomeTeam(homeTeam);
        withRelationsWithoutFields.setAwayTeam(awayTeam);

        MatchDto dtoWithNullNestedFields = matchMapper.toDto(withRelationsWithoutFields);
        assertNull(dtoWithNullNestedFields.getTournamentId());
        assertNull(dtoWithNullNestedFields.getTournamentName());
        assertNull(dtoWithNullNestedFields.getHomeTeamId());
        assertNull(dtoWithNullNestedFields.getHomeTeamName());
        assertNull(dtoWithNullNestedFields.getAwayTeamId());
        assertNull(dtoWithNullNestedFields.getAwayTeamName());
    }

    @Test
    void playerMapperHandlesMissingTeamAndMissingTeamId() {
        Player withoutTeam = new Player();
        withoutTeam.setId(1L);
        withoutTeam.setName("No Team");

        PlayerDto dtoWithoutTeam = playerMapper.toDto(withoutTeam);
        assertEquals(1L, dtoWithoutTeam.getId());
        assertEquals("No Team", dtoWithoutTeam.getName());
        assertNull(dtoWithoutTeam.getTeamId());

        Team teamWithoutId = new Team();
        Player withTeamWithoutId = new Player();
        withTeamWithoutId.setId(2L);
        withTeamWithoutId.setName("Unknown Team Id");
        withTeamWithoutId.setTeam(teamWithoutId);

        PlayerDto dtoWithTeamWithoutId = playerMapper.toDto(withTeamWithoutId);
        assertEquals(2L, dtoWithTeamWithoutId.getId());
        assertEquals("Unknown Team Id", dtoWithTeamWithoutId.getName());
        assertNull(dtoWithTeamWithoutId.getTeamId());
    }

    @Test
    void tournamentMapperHandlesMissingSportAndMissingSportId() {
        Tournament withoutSport = new Tournament();
        withoutSport.setId(88L);
        withoutSport.setName("No Sport");

        TournamentDto dtoWithoutSport = tournamentMapper.toDto(withoutSport);
        assertEquals(88L, dtoWithoutSport.getId());
        assertEquals("No Sport", dtoWithoutSport.getName());
        assertNull(dtoWithoutSport.getSportId());

        Sport sportWithoutId = new Sport();
        Tournament withSportWithoutId = new Tournament();
        withSportWithoutId.setId(89L);
        withSportWithoutId.setName("Sport Without Id");
        withSportWithoutId.setSport(sportWithoutId);

        TournamentDto dtoWithSportWithoutId = tournamentMapper.toDto(withSportWithoutId);
        assertEquals(89L, dtoWithSportWithoutId.getId());
        assertEquals("Sport Without Id", dtoWithSportWithoutId.getName());
        assertNull(dtoWithSportWithoutId.getSportId());
    }
}
