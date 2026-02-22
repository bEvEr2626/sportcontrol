package com.example.sportcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sportcontrol.entity.SportEvent;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<SportEvent, Long> {
    List<SportEvent> findByLocation(String location);
}

