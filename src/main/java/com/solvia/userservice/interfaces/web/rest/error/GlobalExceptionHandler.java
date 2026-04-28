package com.solvia.userservice.interfaces.web.rest.error;

import com.solvia.userservice.application.exception.UnauthenticatedException;
import com.solvia.userservice.application.exception.UserAlreadyExistsException;
import com.solvia.userservice.application.query.exception.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler
 * <p>
 * CENTRALISE LA GESTION DES ERREURS HTTP
 * <p>
 * RESPONSABILITÉS :
 * - mapper exceptions → ApiError
 * - garantir un contrat stable
 * - éviter la fuite des exceptions internes
 * <p>
 * ✔ Stateless
 * ✔ Lisible
 * ✔ Extensible
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ErrorResponseFactory errorFactory;

    public GlobalExceptionHandler(ErrorResponseFactory errorFactory) {
        this.errorFactory = errorFactory;
    }

    // =========================
    // DOMAIN / BUSINESS
    // =========================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return errorFactory.create(ErrorCode.USER_NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return errorFactory.create(ErrorCode.USER_ALREADY_EXISTS, ex.getMessage(), null);
    }

    // =========================
    // SECURITY
    // =========================

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiError> handleUnauthenticated(UnauthenticatedException ex) {
        return errorFactory.create(ErrorCode.UNAUTHENTICATED, ex.getMessage(), null);
    }

    // =========================
    // VALIDATION
    // =========================

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(ConstraintViolationException ex) {

        Map<String, Object> details = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        return errorFactory.create(
                ErrorCode.INVALID_REQUEST,
                "Validation failed",
                details
        );
    }

    // =========================
    // SYSTEM (LAST LINE DEFENSE)
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {

        log.error("Unhandled exception", ex);

        return errorFactory.create(
                ErrorCode.INTERNAL_ERROR,
                "Unexpected error",
                null
        );
    }
}