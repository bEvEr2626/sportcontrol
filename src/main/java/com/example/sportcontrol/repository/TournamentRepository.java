package com.example.sportcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sportcontrol.entity.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

}
