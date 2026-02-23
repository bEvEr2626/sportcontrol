package com.example.sportcontrol.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    /** Record identifier. */
    private Long id;

    /** Name of the event. */
    private String name;

    /** Venue location. */
    private String location;

    /** Date and time of the event. */
    private LocalDateTime date;
}
