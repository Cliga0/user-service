package com.solvia.userservice.domain.authorization;

public interface AuthorizationRule {

    boolean allows(
            AuthorizationContext context,
            Action action,
            Resource resource,
            TupleRepository tuples
    );
}