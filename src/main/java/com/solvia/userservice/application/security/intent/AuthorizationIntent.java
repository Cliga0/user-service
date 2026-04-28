package com.solvia.userservice.application.security.intent;

import com.solvia.userservice.domain.authorization.Action;
import com.solvia.userservice.domain.authorization.Resource;
import com.solvia.userservice.domain.authorization.ResourceType;

/**
 * Catalog of application-level authorization intents.
 * <p>
 * ✔ No business logic
 * ✔ No infrastructure dependency
 * ✔ Centralizes permission vocabulary
 * ✔ Improves readability of use cases
 */
public final class AuthorizationIntent {

    private AuthorizationIntent() {}

    // =========================================================
    // ACTIONS
    // =========================================================

    public static final Action USER_CREATE = Action.of("USER_CREATE");
    public static final Action USER_READ   = Action.of("USER_READ");
    public static final Action USER_UPDATE = Action.of("USER_UPDATE");
    public static final Action USER_DELETE = Action.of("USER_DELETE");

    // =========================================================
    // RESOURCES FACTORIES
    // =========================================================

    public static Resource user(String userId) {
        return Resource.of(ResourceType.of("USER"), userId);
    }

    public static Resource system() {
        return Resource.of(ResourceType.of("SYSTEM"), "SYSTEM");
    }
}