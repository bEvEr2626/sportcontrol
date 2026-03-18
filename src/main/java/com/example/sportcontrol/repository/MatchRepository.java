package com.example.sportcontrol.repository;

import com.example.sportcontrol.dto.MatchFilter;
import com.example.sportcontrol.entity.Match;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    List<Match> findAllBy();

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    Optional<Match> findById(Long id);

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    List<Match> findByLocation(String location);

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    @Query("""
        SELECT m FROM Match m
        JOIN m.tournament t
        JOIN m.homeTeam ht
        JOIN m.awayTeam at
        WHERE (:#{#filter.name} IS NULL OR m.name = :#{#filter.name})
          AND (:#{#filter.location} IS NULL OR m.location = :#{#filter.location})
          AND (:#{#filter.tournamentId} IS NULL OR t.id = :#{#filter.tournamentId})
          AND (:#{#filter.homeTeamName} IS NULL OR ht.name = :#{#filter.homeTeamName})
          AND (:#{#filter.awayTeamName} IS NULL OR at.name = :#{#filter.awayTeamName})
          AND (:#{#filter.dateFrom} IS NULL OR m.date >= :#{#filter.dateFrom})
          AND (:#{#filter.dateTo} IS NULL OR m.date <= :#{#filter.dateTo})
        """)
    Page<Match> findWithFilters(
        @Param("filter") MatchFilter filter,
        Pageable pageable
    );

    @Query(
        value = """
            SELECT m.* FROM matches m
            JOIN teams ht ON ht.id = m.home_team_id
            JOIN teams at ON at.id = m.away_team_id
            JOIN tournaments t ON t.id = m.tournament_id
            WHERE (:#{#filter.name} IS NULL OR m.name = :#{#filter.name})
              AND (:#{#filter.location} IS NULL OR m.location = :#{#filter.location})
              AND (:#{#filter.tournamentId} IS NULL OR t.id = :#{#filter.tournamentId})
              AND (:#{#filter.homeTeamName} IS NULL OR ht.name = :#{#filter.homeTeamName})
              AND (:#{#filter.awayTeamName} IS NULL OR at.name = :#{#filter.awayTeamName})
              AND (:#{#filter.dateFrom} IS NULL OR m.match_date >= :#{#filter.dateFrom})
              AND (:#{#filter.dateTo} IS NULL OR m.match_date <= :#{#filter.dateTo})
        """,
        countQuery = """
            SELECT count(*) FROM matches m
            JOIN teams ht ON ht.id = m.home_team_id
            JOIN teams at ON at.id = m.away_team_id
            JOIN tournaments t ON t.id = m.tournament_id
            WHERE (:#{#filter.name} IS NULL OR m.name = :#{#filter.name})
              AND (:#{#filter.location} IS NULL OR m.location = :#{#filter.location})
              AND (:#{#filter.tournamentId} IS NULL OR t.id = :#{#filter.tournamentId})
              AND (:#{#filter.homeTeamName} IS NULL OR ht.name = :#{#filter.homeTeamName})
              AND (:#{#filter.awayTeamName} IS NULL OR at.name = :#{#filter.awayTeamName})
              AND (:#{#filter.dateFrom} IS NULL OR m.match_date >= :#{#filter.dateFrom})
              AND (:#{#filter.dateTo} IS NULL OR m.match_date <= :#{#filter.dateTo})
        """,
        nativeQuery = true
    )
    Page<Match> findWithFiltersNative(
        @Param("filter") MatchFilter filter,
        Pageable pageable
    );
}