package com.example.sportcontrol.dto;

import lombok.Data;

@Data
public class TournamentDto {

    private Long id;
    private String name;
    private String slug;
    private Long sportId;
}
