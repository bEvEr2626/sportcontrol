package com.example.sportcontrol.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventDto {
    private long id;
    private String name;
    private String location;
    private LocalDateTime date;
}
