package com.example.sportcontrol.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.sportcontrol.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    @EntityGraph(attributePaths = {"team"})
    Page<Player> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {"team"})
    Optional<Player> findById(Long id);

    @Query("SELECT p FROM Player p WHERE p.team.name = :teamName")
    Page<Player> findByTeamName(
        @Param("teamName") String teamName, 
        Pageable pageable);

    @Query(
        value = "SELECT * FROM players p JOIN teams t ON p.team_id = t.id WHERE t.name = :teamName", 
        countQuery = "SELECT COUNT(*) FROM players p JOIN teams t ON p.team_id = t.id WHERE t.name = :teamName",
        nativeQuery = true)
    Page<Player> findByTeamNative(
        @Param("teamName") String teamName,
        Pageable pageable);
}
