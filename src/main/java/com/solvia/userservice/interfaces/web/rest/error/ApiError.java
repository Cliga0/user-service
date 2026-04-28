package com.solvia.userservice.interfaces.web.rest.error;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * ApiError
 * <p>
 * REPRÉSENTATION STANDARD D’UNE ERREUR API
 * <p>
 * ✔ Immutable
 * ✔ Machine-readable
 * ✔ Observability-friendly
 * ✔ Stable contract
 */
public record ApiError(

        String type,
        ErrorCategory category,
        String message,
        boolean retryable,
        UUID correlationId,
        Instant timestamp,
        Map<String, Object> details

) implements Serializable {

    /**
     * Factory method standard (sans details)
     */
    public static ApiError of(
            ErrorCode errorCode,
            String message,
            UUID correlationId
    ) {
        return new ApiError(
                errorCode.code(),
                errorCode.category(),
                message,
                errorCode.retryable(),
                correlationId,
                Instant.now(),
                Map.of()
        );
    }

    /**
     * Factory method avancée (avec details)
     */
    public static ApiError of(
            ErrorCode errorCode,
            String message,
            UUID correlationId,
            Map<String, Object> details
    ) {
        return new ApiError(
                errorCode.code(),
                errorCode.category(),
                message,
                errorCode.retryable(),
                correlationId,
                Instant.now(),
                details == null ? Map.of() : Map.copyOf(details)
        );
    }
}