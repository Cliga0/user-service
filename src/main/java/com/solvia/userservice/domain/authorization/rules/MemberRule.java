package com.solvia.userservice.domain.authorization.rules;

import com.solvia.userservice.domain.authorization.*;

public final class MemberRule implements AuthorizationRule {

    @Override
    public boolean allows(
            AuthorizationContext context,
            Action action,
            Resource resource,
            TupleRepository tuples
    ) {

        if (!action.isRead()) {
            return false;
        }

        return tuples.exists(
                context.subject(),
                Relation.MEMBER,
                resource
        );
    }
}