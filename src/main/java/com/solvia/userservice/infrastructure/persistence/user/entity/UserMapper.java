package com.solvia.userservice.infrastructure.persistence.user.entity;

import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.address.*;
import com.solvia.userservice.domain.model.vo.business.*;
import com.solvia.userservice.domain.model.vo.contact.*;
import com.solvia.userservice.domain.model.vo.identity.*;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata.UserStatus;
import com.solvia.userservice.domain.model.vo.person.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UserMapper
 *
 * <p>
 * Mapping pur entre Domain et Persistence.
 *
 * <p>
 * - Stateless
 * - Thread-safe
 * - No business logic
 * - Ultra performant
 */
public final class UserMapper {

    private UserMapper() {}

    // =========================
    // DOMAIN -> ENTITY
    // =========================

    public static UserEntity toEntity(User user) {
        Objects.requireNonNull(user);

        UserEntity entity = new UserEntity();

        entity.setId(user.id().value());
        entity.setExternalAuthId(
                user.externalAuthId() != null ? user.externalAuthId().value() : null
        );

        entity.setFirstName(user.firstName().value());
        entity.setLastName(user.lastName().value());
        entity.setBirthDate(user.birthDate().value());
        entity.setGender(user.gender().asString());

        entity.setEmail(user.email() != null ? user.email().value() : null);
        entity.setPhoneNumber(user.phoneNumber() != null ? user.phoneNumber().value() : null);

        entity.setAddresses(
                user.addresses().stream()
                        .map(UserMapper::toEmbeddable)
                        .collect(Collectors.toList())
        );

        entity.setRole(user.role().asString());
        entity.setBalance(user.balance().value());
        entity.setLoyaltyPoints(user.loyaltyPoints().value());

        entity.setStatus(user.metadata().status().name());
        entity.setCreatedAt(user.metadata().createdAt());
        entity.setUpdatedAt(user.metadata().updatedAt());
        entity.setCreatedBy(user.metadata().createdBy().asString());
        entity.setUpdatedBy(user.metadata().updatedBy().asString());

        return entity;
    }

    // =========================
    // ENTITY -> DOMAIN
    // =========================

    public static User toDomain(UserEntity entity) {
        Objects.requireNonNull(entity);

        return User.rehydrate(
                UserId.of(entity.getId()),
                entity.getExternalAuthId() != null
                        ? ExternalAuthId.of(entity.getExternalAuthId())
                        : null,

                FirstName.of(entity.getFirstName()),
                LastName.of(entity.getLastName()),
                BirthDate.of(entity.getBirthDate()),
                Gender.fromString(entity.getGender()),

                entity.getEmail() != null ? Email.of(entity.getEmail()) : null,
                entity.getPhoneNumber() != null ? PhoneNumber.of(entity.getPhoneNumber()) : null,

                mapAddresses(entity.getAddresses()),

                UserRole.of(UserRole.Code.from(entity.getRole())),

                AccountBalance.of(entity.getBalance()),
                LoyaltyPoints.of(entity.getLoyaltyPoints()),

                mapMetadata(entity)
        );
    }

    // =========================
    // ADDRESS
    // =========================

    private static AddressEmbeddable toEmbeddable(Address address) {
        return new AddressEmbeddable(
                address.street().value(),
                address.city().value(),
                address.country().code(),
                address.postalCode().value()
        );
    }

    private static List<Address> mapAddresses(List<AddressEmbeddable> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(a -> Address.of(
                        Street.of(a.getStreet()),
                        City.of(a.getCity()),
                        PostalCode.of(a.getPostalCode()),
                        Country.of(a.getCountry())
                ))
                .collect(Collectors.toList());
    }

    // =========================
    // METADATA
    // =========================

    private static UserMetadata mapMetadata(UserEntity entity) {
        return UserMetadata.rehydrate(
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                ActorId.fromString(entity.getCreatedBy()),
                ActorId.fromString(entity.getUpdatedBy()),
                UserStatus.valueOf(entity.getStatus())
        );
    }
}
