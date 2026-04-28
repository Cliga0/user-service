package com.solvia.userservice.domain.authorization.rules;

import com.solvia.userservice.domain.authorization.*;

public final class OwnerRule implements AuthorizationRule {

    @Override
    public boolean allows(
            AuthorizationContext context,
            Action action,
            Resource resource,
            TupleRepository tuples
    ) {
        return tuples.exists(
                context.subject(),
                Relation.OWNER,
                resource
        );
    }
}