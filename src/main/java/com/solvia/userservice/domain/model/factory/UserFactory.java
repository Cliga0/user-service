package com.solvia.userservice.domain.model.factory;

import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.contact.PhoneNumber;
import com.solvia.userservice.domain.model.vo.identity.ExternalAuthId;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.person.*;

import java.util.Objects;

/**
 * DOMAIN FACTORY (PURE)
 * <p>
 * ❌ No DTO
 * ❌ No Command
 * ❌ No mapping logic
 * <p>
 * ✔ Only Value Objects
 * ✔ Only domain creation rules
 */
public final class UserFactory {

    public User create(
            UserId userId,
            ExternalAuthId externalAuthId,
            FirstName firstName,
            LastName lastName,
            BirthDate birthDate,
            Gender gender,
            Email email,
            PhoneNumber phoneNumber,
            UserRole role,
            ActorId actorId
    ) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(birthDate);
        Objects.requireNonNull(gender);
        Objects.requireNonNull(role);
        Objects.requireNonNull(actorId);

        return User.create(
                userId,
                externalAuthId,
                firstName,
                lastName,
                birthDate,
                gender,
                email,
                phoneNumber,
                role,
                actorId
        );
    }
}