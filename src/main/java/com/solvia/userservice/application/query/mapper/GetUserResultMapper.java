package com.solvia.userservice.application.query.mapper;

import com.solvia.userservice.application.query.result.GetUserResult;
import com.solvia.userservice.domain.model.aggregate.User;

import java.util.Objects;

/**
 * Anti-Corruption Mapper (DOMAIN → READ MODEL)
 * <p>
 * Responsabilité :
 * - découpler complètement le domain du query layer
 * - stabiliser le contrat API
 * - éviter les dépendances aux Value Objects
 */
public final class GetUserResultMapper {

    public GetUserResult toResult(User user) {

        Objects.requireNonNull(user, "user must not be null");

        return GetUserResult.of(
                user.id().asString(),
                user.firstName().value(),
                user.lastName().value(),
                user.birthDate().value().toString(),
                safe(user.gender()),
                safe(user.email()),
                safe(user.phoneNumber()),
                safe(user.role()),
                safe(user.metadata().status())
        );
    }

    private String safe(Object value) {
        return value != null ? value.toString() : null;
    }
}