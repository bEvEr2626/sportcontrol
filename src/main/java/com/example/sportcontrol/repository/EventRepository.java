package com.example.sportcontrol.repository;

import com.example.sportcontrol.entity.SportEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for SportEvent entity.
 */
@Repository
public interface EventRepository extends JpaRepository<SportEvent, Long> {
    /**
     * Finds events by their location.
     *
     * @param location the location to search for.
     * @return a list of sport events.
     */
    List<SportEvent> findByLocation(String location);
}
