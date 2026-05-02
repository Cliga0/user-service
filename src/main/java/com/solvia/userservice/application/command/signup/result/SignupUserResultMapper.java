package com.solvia.userservice.application.command.signup.result;

import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata;

import java.util.Objects;

/**
 * Maps User aggregate → SignupUserResult
 * <p>
 * APPLICATION MAPPER
 * <p>
 * RESPONSIBILITIES:
 * - Build stable application read model
 * - Prevent domain leakage
 * <p>
 * NON-RESPONSIBILITIES:
 * - No business logic
 * - No formatting logic
 * - No infrastructure concerns
 */
public final class SignupUserResultMapper {

    public SignupUserResult map(User user) {

        Objects.requireNonNull(user, "User must not be null");

        return new SignupUserResult(
                user.id().value(),
                user.email().value(),
                user.phoneNumber().value(),
                user.firstName().value(),
                user.lastName().value(),
                user.birthDate().iso(),
                user.gender().asString(),
                user.role().value(),
                user.metadata().status() == UserMetadata.UserStatus.ACTIVE,
                user.metadata().createdAt()
        );
    }
}