package com.solvia.userservice.domain.authorization;

import java.util.List;
import java.util.Objects;

public final class AuthorizationEngine {

    private final List<AuthorizationRule> rules;
    private final TupleRepository tuples;

    public AuthorizationEngine(
            List<AuthorizationRule> rules,
            TupleRepository tuples
    ) {
        this.rules = List.copyOf(rules);
        this.tuples = Objects.requireNonNull(tuples);
    }

    public boolean isAllowed(
            AuthorizationContext context,
            Action action,
            Resource resource
    ) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(action);
        Objects.requireNonNull(resource);

        for (AuthorizationRule rule : rules) {
            if (rule.allows(context, action, resource, tuples)) {
                return true;
            }
        }


        return false;
    }
}