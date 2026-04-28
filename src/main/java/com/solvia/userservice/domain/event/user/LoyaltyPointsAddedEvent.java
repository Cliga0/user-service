package com.solvia.userservice.domain.event.user;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.EventType;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.domain.model.vo.business.LoyaltyPointsReason;

import java.util.Objects;

/**
 * LoyaltyPointsAddedEvent
 *
 * <p>
 * Événement métier déclenché lorsqu'un utilisateur reçoit des points de fidélité.
 *
 * <p>
 * Critique pour audit, gamification et anti-fraude.
 */
public final class LoyaltyPointsAddedEvent implements DomainEvent {

    private static final EventType TYPE = EventType.of("user.loyalty.points.added.v1");

    private final EventMetadata metadata;

    private final UserId userId;
    private final int pointsAdded;
    private final int newTotal;
    private final LoyaltyPointsReason reason;

    private LoyaltyPointsAddedEvent(
        EventMetadata metadata,
        UserId userId,
        int pointsAdded,
        int newTotal,
        LoyaltyPointsReason reason
    ) {
        this.metadata = Objects.requireNonNull(metadata);
        this.userId = Objects.requireNonNull(userId);
        this.pointsAdded = pointsAdded;
        this.newTotal = newTotal;
        this.reason = Objects.requireNonNull(reason);

        validate();
    }

    // =========================
    // FACTORY
    // =========================

    public static LoyaltyPointsAddedEvent of(
        EventMetadata metadata,
        UserId userId,
        int pointsAdded,
        int newTotal,
        LoyaltyPointsReason reason
    ) {
        return new LoyaltyPointsAddedEvent(
            metadata,
            userId,
            pointsAdded,
            newTotal,
            reason
        );
    }

    // =========================
    // VALIDATION
    // =========================

    private void validate() {
        if (pointsAdded <= 0) {
            throw new IllegalArgumentException("Points added must be positive");
        }

        if (newTotal < pointsAdded) {
            throw new IllegalStateException("New total cannot be less than points added");
        }
    }

    // =========================
    // DOMAIN EVENT
    // =========================

    @Override
    public EventMetadata metadata() {
        return metadata;
    }

    @Override
    public EventType eventType() {
        return TYPE;
    }

    // =========================
    // GETTERS
    // =========================

    public UserId userId() {
        return userId;
    }

    public int pointsAdded() {
        return pointsAdded;
    }

    public int newTotal() {
        return newTotal;
    }

    public LoyaltyPointsReason reason() {
        return reason;
    }
}
