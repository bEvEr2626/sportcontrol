package com.example.sportcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sportcontrol.entity.Sport;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {

}
