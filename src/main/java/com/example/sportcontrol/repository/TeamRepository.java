package com.example.sportcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sportcontrol.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
