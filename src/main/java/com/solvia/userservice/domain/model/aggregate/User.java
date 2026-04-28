package com.solvia.userservice.domain.model.aggregate;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadataFactory;
import com.solvia.userservice.domain.event.user.*;
import com.solvia.userservice.domain.model.vo.address.Address;
import com.solvia.userservice.domain.model.vo.business.*;
import com.solvia.userservice.domain.model.vo.contact.*;
import com.solvia.userservice.domain.model.vo.identity.*;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.business.LoyaltyPointsReason;
import com.solvia.userservice.domain.model.vo.metadata.SuspensionReason;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata.UserStatus;
import com.solvia.userservice.domain.model.vo.person.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * User Aggregate Root
 *
 * <p>
 * - Encapsulation forte
 * - Invariants métier garantis
 * - Event-driven
 * - DDD pur
 * - Production-ready
 */
public class User implements Serializable {

    // =========================
    // IDENTITY
    // =========================

    private final UserId id;
    private final ExternalAuthId externalAuthId;

    // =========================
    // PERSON
    // =========================

    private FirstName firstName;
    private LastName lastName;
    private BirthDate birthDate;
    private Gender gender;

    // =========================
    // CONTACT
    // =========================

    private Email email;
    private PhoneNumber phoneNumber;

    // =========================
    // ADDRESS
    // =========================

    private final List<Address> addresses;

    // =========================
    // BUSINESS
    // =========================

    private UserRole role;
    private AccountBalance balance;
    private LoyaltyPoints loyaltyPoints;

    // =========================
    // METADATA
    // =========================

    private UserMetadata metadata;

    // =========================
    // EVENTS BUFFER
    // =========================

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();

    // =========================
    // CONSTRUCTOR
    // =========================

    private User(
        UserId id,
        ExternalAuthId externalAuthId,
        FirstName firstName,
        LastName lastName,
        BirthDate birthDate,
        Gender gender,
        Email email,
        PhoneNumber phoneNumber,
        List<Address> addresses,
        UserRole role,
        AccountBalance balance,
        LoyaltyPoints loyaltyPoints,
        UserMetadata metadata
    ) {
        this.id = Objects.requireNonNull(id);
        this.externalAuthId = externalAuthId;

        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.birthDate = Objects.requireNonNull(birthDate);
        this.gender = Objects.requireNonNull(gender);

        this.email = email;
        this.phoneNumber = phoneNumber;

        this.addresses = addresses == null ? new ArrayList<>() : new ArrayList<>(addresses);

        this.role = Objects.requireNonNull(role);
        this.balance = Objects.requireNonNull(balance);
        this.loyaltyPoints = Objects.requireNonNull(loyaltyPoints);

        this.metadata = Objects.requireNonNull(metadata);

        assertValidState();
    }

    // =========================
    // FACTORY (AGGREGATE CREATION)
    // =========================

    public static User create(
        UserId id,
        ExternalAuthId externalAuthId,
        FirstName firstName,
        LastName lastName,
        BirthDate birthDate,
        Gender gender,
        Email email,
        PhoneNumber phoneNumber,
        UserRole role,
        ActorId actor
    ) {

        User user = new User(
            id,
            externalAuthId,
            firstName,
            lastName,
            birthDate,
            gender,
            email,
            phoneNumber,
            List.of(),
            role,
            AccountBalance.zero(),
            LoyaltyPoints.zero(),
            UserMetadata.create(actor)
        );

        user.record(
            UserCreatedEvent.of(
                EventMetadataFactory.now(actor),
                id,
                email,
                user.metadata.status()
            )
        );

        return user;
    }

    public static User rehydrate(
        UserId id,
        ExternalAuthId externalAuthId,
        FirstName firstName,
        LastName lastName,
        BirthDate birthDate,
        Gender gender,
        Email email,
        PhoneNumber phoneNumber,
        List<Address> addresses,
        UserRole role,
        AccountBalance balance,
        LoyaltyPoints loyaltyPoints,
        UserMetadata metadata
    ) {
        return new User(
            id,
            externalAuthId,
            firstName,
            lastName,
            birthDate,
            gender,
            email,
            phoneNumber,
            addresses,
            role,
            balance,
            loyaltyPoints,
            metadata
        );
    }

    // =========================
    // BUSINESS METHODS
    // =========================

    public void changeEmail(Email newEmail, ActorId actor) {

        if (Objects.equals(this.email, newEmail)) return;

        Email old = this.email;
        this.email = newEmail;

        touch(actor);

        record(UserEmailChangedEvent.of(
            EventMetadataFactory.now(actor),
            this.id,
            old,
            newEmail
        ));
    }

    public void addLoyaltyPoints(int points, LoyaltyPointsReason reason, ActorId actor) {

        this.loyaltyPoints = this.loyaltyPoints.add(points);

        touch(actor);

        record(LoyaltyPointsAddedEvent.of(
            EventMetadataFactory.now(actor),
            this.id,
            points,
            this.loyaltyPoints.value(),
            reason
        ));
    }

    public void suspend(SuspensionReason reason, Instant until, ActorId actor) {

        this.metadata = metadata.changeStatus(UserStatus.SUSPENDED, actor);

        touch(actor);

        record(UserSuspendedEvent.of(
            EventMetadataFactory.now(actor),
            this.id,
            reason,
            until
        ));
    }


    public void activate(ActorId actor) {
        this.metadata = metadata.changeStatus(UserStatus.ACTIVE, actor);
        touch(actor);
    }

    public void delete(ActorId actor) {
        this.metadata = metadata.changeStatus(UserStatus.DELETED, actor);
        touch(actor);
    }

    public void creditBalance(BigDecimal amount, ActorId actor) {
        this.balance = this.balance.add(amount);
        touch(actor);
    }

    public void debitBalance(BigDecimal amount, ActorId actor) {
        this.balance = this.balance.subtract(amount);
        touch(actor);
    }

    // =========================
    // EVENT HANDLING
    // =========================

    private void record(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullEvents() {
        List<DomainEvent> copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    // =========================
    // INVARIANTS ENGINE
    // =========================

    private void assertValidState() {

        if (email == null && phoneNumber == null) {
            throw new IllegalStateException("User must have email or phone");
        }

        if (balance.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Balance cannot be negative");
        }

        if (loyaltyPoints.value() < 0) {
            throw new IllegalStateException("Loyalty points cannot be negative");
        }

        if (metadata.status() == UserStatus.DELETED) {
            // hard lock state possible
        }
    }

    private void touch(ActorId actor) {
        this.metadata = metadata.update(actor);
        assertValidState();
    }

    // =========================
    // READ API
    // =========================

    public UserId id() { return id; }

    public Email email() { return email; }

    public List<Address> addresses() { return Collections.unmodifiableList(addresses);}

    public AccountBalance balance() { return balance; }

    public LoyaltyPoints loyaltyPoints() { return loyaltyPoints; }

    public UserMetadata metadata() { return metadata;}

    public FirstName firstName() { return firstName; }

    public LastName lastName() { return lastName; }

    public BirthDate birthDate() { return birthDate; }

    public Gender gender() { return gender; }

    public PhoneNumber phoneNumber() { return phoneNumber; }

    public UserRole role() { return role; }

    public ExternalAuthId externalAuthId() { return externalAuthId; }
}
