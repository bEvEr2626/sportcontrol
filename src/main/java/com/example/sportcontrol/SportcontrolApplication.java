package com.example.sportcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Sport Control.
 */
@SpringBootApplication
public class SportcontrolApplication {

    /**
     * Protected constructor to satisfy Checkstyle.
     */
    protected SportcontrolApplication() {
    }

    /**
     * Main entry point of the application.
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(SportcontrolApplication.class, args);
    }
}
