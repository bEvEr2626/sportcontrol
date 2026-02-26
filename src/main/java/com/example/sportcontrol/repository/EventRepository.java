package com.example.sportcontrol.repository;

import com.example.sportcontrol.entity.SportEvent;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<SportEvent, Long> {
    List<SportEvent> findByLocation(String location);
}
