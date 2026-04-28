package com.solvia.userservice.interfaces.web.rest.error;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ErrorResponseFactory {

    private final CorrelationIdProvider correlationIdProvider;

    public ErrorResponseFactory(CorrelationIdProvider correlationIdProvider) {
        this.correlationIdProvider = correlationIdProvider;
    }

    public ResponseEntity<ApiError> create(
            ErrorCode code,
            String message,
            Map<String, Object> details
    ) {

        ApiError error = ApiError.of(
                code,
                message,
                correlationIdProvider.get(),
                details
        );

        return ResponseEntity
                .status(code.httpStatus())
                .body(error);
    }
}