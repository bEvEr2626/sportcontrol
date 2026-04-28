package com.example.sportcontrol.config;

import com.example.sportcontrol.mapper.MatchMapper;
import com.example.sportcontrol.mapper.PlayerMapper;
import com.example.sportcontrol.mapper.SportMapper;
import com.example.sportcontrol.mapper.TeamMapper;
import com.example.sportcontrol.mapper.TournamentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapperConfig {

    @Bean
    @Primary
    public MatchMapper matchMapper() {
        return Mappers.getMapper(MatchMapper.class);
    }

    @Bean
    @Primary
    public PlayerMapper playerMapper() {
        return Mappers.getMapper(PlayerMapper.class);
    }

    @Bean
    @Primary
    public SportMapper sportMapper() {
        return Mappers.getMapper(SportMapper.class);
    }

    @Bean
    @Primary
    public TeamMapper teamMapper() {
        return Mappers.getMapper(TeamMapper.class);
    }

    @Bean
    @Primary
    public TournamentMapper tournamentMapper() {
        return Mappers.getMapper(TournamentMapper.class);
    }
}
