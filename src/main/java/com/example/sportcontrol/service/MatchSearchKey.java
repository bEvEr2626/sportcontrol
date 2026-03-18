package com.example.sportcontrol.service;

import com.example.sportcontrol.dto.MatchFilter;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchSearchKey {

    private String name;
    private String location;
    private Long tournamentId;
    private String homeTeamName;
    private String awayTeamName;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private int page;
    private int size;

    public MatchSearchKey(MatchFilter filter, int page, int size) {
        this.name = filter.getName();
        this.location = filter.getLocation();
        this.tournamentId = filter.getTournamentId();
        this.homeTeamName = filter.getHomeTeamName();
        this.awayTeamName = filter.getAwayTeamName();
        this.dateFrom = filter.getDateFrom();
        this.dateTo = filter.getDateTo();
        this.page = page;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchSearchKey)) return false;
        MatchSearchKey that = (MatchSearchKey) o;
        return page == that.page &&
               size == that.size &&
               Objects.equals(name, that.name) &&
               Objects.equals(location, that.location) &&
               Objects.equals(tournamentId, that.tournamentId) &&
               Objects.equals(homeTeamName, that.homeTeamName) &&
               Objects.equals(awayTeamName, that.awayTeamName) &&
               Objects.equals(dateFrom, that.dateFrom) &&
               Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                location,
                tournamentId,
                homeTeamName,
                awayTeamName,
                dateFrom,
                dateTo,
                page,
                size
        );
    }
}