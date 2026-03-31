package com.example.sportcontrol.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.sportcontrol.service..*(..))")
    public Object logServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startedAt = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedNanos = System.nanoTime() - startedAt;
            double elapsedMs = elapsedNanos / 1_000_000.0;
            if (LOG.isInfoEnabled()) {
                LOG.info("{} executed in {} ms",
                        joinPoint.getSignature().toShortString(),
                        String.format("%.3f", elapsedMs));
            }
        }
    }
}