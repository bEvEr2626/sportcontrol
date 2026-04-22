package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.sportcontrol.dto.RaceConditionDemoDto;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class RaceConditionDemoServiceTest {

    private final RaceConditionDemoService service = new RaceConditionDemoService();

    @Test
    void runDemoThrowsWhenThreadsBelowMinimum() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.runDemo(49, 100)
        );

        assertEquals("Threads must be >= 50 for race condition demo", exception.getMessage());
    }

    @Test
    void runDemoThrowsWhenIncrementsPerThreadIsNotPositive() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.runDemo(50, 0)
        );

        assertEquals("incrementsPerThread must be > 0", exception.getMessage());
    }

    @Test
    void runDemoReturnsExpectedSynchronizedAndAtomicCounters() {
        int threads = 50;
        int incrementsPerThread = 200;

        RaceConditionDemoDto result = service.runDemo(threads, incrementsPerThread);

        int expected = threads * incrementsPerThread;
        assertEquals(threads, result.getThreads());
        assertEquals(incrementsPerThread, result.getIncrementsPerThread());
        assertEquals(expected, result.getExpected());
        assertEquals(expected, result.getSynchronizedCounter());
        assertEquals(expected, result.getAtomicCounter());
        assertTrue(result.getUnsafeCounter() <= expected);
        assertEquals(
            service.isRaceConditionDetected(
                result.getUnsafeCounter(),
                result.getSynchronizedCounter(),
                result.getAtomicCounter(),
                expected
            ),
            result.isRaceConditionDetected()
        );
    }

    @Test
    void awaitCompletionThrowsWhenTimedOut() {
        CountDownLatch latch = new CountDownLatch(1);

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> service.awaitCompletion(latch, 0, TimeUnit.MILLISECONDS)
        );

        assertEquals("Race condition demo timed out", exception.getMessage());
    }

    @Test
    void awaitCompletionThrowsWhenCurrentThreadIsInterrupted() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread.currentThread().interrupt();
        try {
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.awaitCompletion(latch, 1, TimeUnit.SECONDS)
            );

            assertEquals("Race condition demo interrupted", exception.getMessage());
            assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void awaitStartSignalReturnsTrueWhenLatchIsAlreadyReleased() {
        CountDownLatch latch = new CountDownLatch(0);

        assertTrue(service.awaitStartSignal(latch));
    }

    @Test
    void awaitStartSignalReturnsFalseWhenCurrentThreadIsInterrupted() {
        CountDownLatch latch = new CountDownLatch(1);

        Thread.currentThread().interrupt();
        try {
            assertFalse(service.awaitStartSignal(latch));
            assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void runDemoHandlesInterruptedWorkersAndStillCompletes() {
        RaceConditionDemoService interruptedWorkerService = new RaceConditionDemoService() {
            @Override
            boolean shouldRunIncrements(CountDownLatch startedLatch) {
                return false;
            }
        };

        RaceConditionDemoDto result = interruptedWorkerService.runDemo(50, 10);

        assertEquals(500, result.getExpected());
        assertEquals(0, result.getUnsafeCounter());
        assertEquals(0, result.getSynchronizedCounter());
        assertEquals(0, result.getAtomicCounter());
        assertFalse(result.isRaceConditionDetected());
    }

    @Test
    void isRaceConditionDetectedCoversAllBooleanPaths() {
        assertTrue(service.isRaceConditionDetected(9, 10, 10, 10));
        assertFalse(service.isRaceConditionDetected(10, 10, 10, 10));
        assertFalse(service.isRaceConditionDetected(9, 9, 10, 10));
        assertFalse(service.isRaceConditionDetected(9, 10, 9, 10));
    }
}
