package com.example.sportcontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sportcontrol.entity.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @EntityGraph(attributePaths = {"sport"})
    List<Tournament> findAllBy();

    @EntityGraph(attributePaths = {"sport"})
    Optional<Tournament> findById(Long id);

}
