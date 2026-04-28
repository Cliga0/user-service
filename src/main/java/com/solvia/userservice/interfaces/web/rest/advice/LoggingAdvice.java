package com.solvia.userservice.interfaces.web.rest.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;

/**
 * LoggingAdvice
 * <p>
 * CROSS-CUTTING OBSERVABILITY LAYER
 * <p>
 * RESPONSIBILITIES:
 * - request lifecycle tracing
 * - correlationId propagation
 * - execution time measurement
 * - error classification logging
 * <p>
 * NON RESPONSIBILITIES:
 * - no business logic
 * - no request mutation
 * - no payload logging (security)
 */
@Aspect
public class LoggingAdvice {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAdvice.class);

    // =========================================
    // INTERCEPT ALL REST CONTROLLERS
    // =========================================

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        Instant start = Instant.now();

        String method = joinPoint.getSignature().toShortString();
        String correlationId = MDC.get("correlationId");

        try {

            log.info("REQUEST_START method={} correlationId={}",
                    method,
                    correlationId
            );

            Object result = joinPoint.proceed();

            long duration = Duration.between(start, Instant.now()).toMillis();

            log.info("REQUEST_SUCCESS method={} correlationId={} durationMs={}",
                    method,
                    correlationId,
                    duration
            );

            return result;

        } catch (Exception ex) {

            long duration = Duration.between(start, Instant.now()).toMillis();

            log.error("REQUEST_FAILURE method={} correlationId={} durationMs={} exception={}",
                    method,
                    correlationId,
                    duration,
                    ex.getClass().getSimpleName(),
                    ex
            );

            throw ex;
        }
    }
}