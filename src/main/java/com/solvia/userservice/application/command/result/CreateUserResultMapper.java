package com.solvia.userservice.application.command.result;

import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.business.UserRole;

import java.time.Instant;

public final class CreateUserResultMapper {

    public CreateUserResult map(User user) {
        return new CreateUserResult(
                user.id().value(),
                user.email().value(),
                resolveRole(user.role()),
                true,
                resolveCreatedAt(user),
                buildSelfLink(user)
        );
    }

    private String resolveRole(UserRole role) {
        return role.code().name();
    }

    private Instant resolveCreatedAt(User user) {
        return user.metadata().createdAt();
    }

    private String buildSelfLink(User user) {
        return "/users/" + user.id().value();
    }
}