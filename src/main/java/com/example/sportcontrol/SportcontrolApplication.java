package com.example.sportcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SportcontrolApplication {

    protected SportcontrolApplication() {
    }

    public static void main(final String[] args) {
        SpringApplication.run(SportcontrolApplication.class, args);
    }
}
