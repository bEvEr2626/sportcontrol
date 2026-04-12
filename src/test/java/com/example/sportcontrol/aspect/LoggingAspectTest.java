package com.example.sportcontrol.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    private final LoggingAspect loggingAspect = new LoggingAspect();

    @Test
    void logServiceExecutionTimeReturnsProceedResult() throws Throwable {
        when(joinPoint.proceed()).thenReturn("ok");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("Service.method(..)");

        Object result = loggingAspect.logServiceExecutionTime(joinPoint);

        assertEquals("ok", result);
    }

    @Test
    void logServiceExecutionTimeRethrowsException() throws Throwable {
        RuntimeException exception = new RuntimeException("boom");
        when(joinPoint.proceed()).thenThrow(exception);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("Service.method(..)");

        RuntimeException thrown = assertThrows(
            RuntimeException.class,
            () -> loggingAspect.logServiceExecutionTime(joinPoint)
        );

        assertEquals("boom", thrown.getMessage());
    }
}
