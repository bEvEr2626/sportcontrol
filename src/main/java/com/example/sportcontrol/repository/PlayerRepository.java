package com.example.sportcontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sportcontrol.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    @EntityGraph(attributePaths = {"team"})
    List<Player> findAllBy();

    @EntityGraph(attributePaths = {"team"})
    Optional<Player> findById(Long id);
}
