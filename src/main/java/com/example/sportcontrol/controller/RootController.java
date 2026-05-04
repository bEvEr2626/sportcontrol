package com.example.sportcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Root", description = "Root endpoint")
public class RootController {

    @GetMapping("/")
    @Operation(summary = "Welcome message", description = "Returns a simple welcome message")
    public String welcome() {
        return "welcome to sportcontrol";
    }
}
