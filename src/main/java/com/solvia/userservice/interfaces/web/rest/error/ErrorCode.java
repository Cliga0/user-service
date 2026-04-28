package com.solvia.userservice.interfaces.web.rest.error;

/**
 * ErrorCode
 * <p>
 * CONTRAT D’ERREUR STABLE (API)
 * <p>
 * RESPONSABILITÉS :
 * - définir des codes métiers et techniques
 * - associer un HTTP status
 * - rester stable dans le temps
 * <p>
 * ⚠️ NE JAMAIS CHANGER UNE VALEUR EXISTANTE
 */
public enum ErrorCode {

    // BUSINESS
    USER_NOT_FOUND("USER_NOT_FOUND", 404, false, ErrorCategory.BUSINESS),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", 409, false, ErrorCategory.BUSINESS),

    // VALIDATION
    INVALID_REQUEST("INVALID_REQUEST", 400, false, ErrorCategory.VALIDATION),

    // AUTH
    UNAUTHENTICATED("UNAUTHENTICATED", 401, false, ErrorCategory.AUTHORIZATION),
    FORBIDDEN("FORBIDDEN", 403, false, ErrorCategory.AUTHORIZATION),

    // SYSTEM
    INTERNAL_ERROR("INTERNAL_ERROR", 500, true, ErrorCategory.SYSTEM);

    private final String code;
    private final int httpStatus;
    private final boolean retryable;
    private final ErrorCategory category;

    ErrorCode(String code, int httpStatus, boolean retryable, ErrorCategory category) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.retryable = retryable;
        this.category = category;
    }

    public String code() { return code; }
    public int httpStatus() { return httpStatus; }
    public boolean retryable() { return retryable; }
    public ErrorCategory category() { return category; }
}