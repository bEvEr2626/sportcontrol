package com.example.sportcontrol.repository;

import com.example.sportcontrol.entity.Match;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByLocation(String location);
}
