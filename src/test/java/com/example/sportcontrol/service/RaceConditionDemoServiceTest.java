package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.sportcontrol.dto.RaceConditionDemoDto;

class RaceConditionDemoServiceTest {

    private final RaceConditionDemoService service = new RaceConditionDemoService();

    @Test
    void runDemo_shouldDetectRaceAndShowThreadSafeSolutions() {
        RaceConditionDemoDto result = service.runDemo(64, 10000);

        assertEquals(640000, result.getExpected());
        assertTrue(result.getUnsafeCounter() < result.getExpected());
        assertEquals(result.getExpected(), result.getSynchronizedCounter());
        assertEquals(result.getExpected(), result.getAtomicCounter());
        assertTrue(result.isRaceConditionDetected());
    }

    @Test
    void runDemo_shouldRejectThreadsBelow50() {
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(49, 1000));
    }

    @Test
    void runDemo_shouldRejectNonPositiveIncrements() {
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(50, 0));
    }
}