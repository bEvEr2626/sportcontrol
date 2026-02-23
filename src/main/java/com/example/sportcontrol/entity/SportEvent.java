package com.example.sportcontrol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a sport event.
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class SportEvent {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the event. */
    private String name;

    /** Location where the event takes place. */
    private String location;

    /** Date and time of the event. */
    @Column(name = "date")
    private LocalDateTime date;
}
