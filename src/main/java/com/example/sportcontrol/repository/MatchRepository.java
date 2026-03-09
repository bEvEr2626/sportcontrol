package com.example.sportcontrol.repository;

import com.example.sportcontrol.entity.Match;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    List<Match> findAllBy();

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    Optional<Match> findById(Long id);

    @EntityGraph(attributePaths = {"tournament", "homeTeam", "awayTeam"})
    List<Match> findByLocation(String location);
}
