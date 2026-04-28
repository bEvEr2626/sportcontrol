package com.example.sportcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SportcontrolApplication {

    @FunctionalInterface
    interface ApplicationRunner {
        void run(Class<?> applicationClass, String... args);
    }

    static ApplicationRunner applicationRunner = SpringApplication::run;

    protected SportcontrolApplication() {
    }

    public static void main(final String[] args) {
        applicationRunner.run(SportcontrolApplication.class, args);
    }
}
