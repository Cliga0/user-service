package com.solvia.userservice.infrastructure.messaging.outbox.entity;

import com.solvia.userservice.infrastructure.messaging.outbox.OutboxEventStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_outbox_status", columnList = "status"),
                @Index(name = "idx_outbox_created_at", columnList = "created_at"),
                @Index(name = "idx_outbox_aggregate", columnList = "aggregate_id"),
                @Index(name = "idx_outbox_tenant", columnList = "tenant_id")
        }
)
public class OutboxEventEntity {

    // ---------------------------------------------------------------------
    // Identity
    // ---------------------------------------------------------------------

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // ---------------------------------------------------------------------
    // Aggregate reference
    // ---------------------------------------------------------------------

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    // ---------------------------------------------------------------------
    // Event metadata
    // ---------------------------------------------------------------------

    @Column(name = "event_type", nullable = false, length = 150)
    private String eventType;

    @Column(name = "event_version", nullable = false, length = 20)
    private String eventVersion;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "trace_id", nullable = false, length = 100)
    private String traceId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    // ---------------------------------------------------------------------
    // Payload & headers
    // ---------------------------------------------------------------------

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Lob
    @Column(name = "headers_json")
    private String headersJson;

    // ---------------------------------------------------------------------
    // Status & retry
    // ---------------------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxEventStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    // ---------------------------------------------------------------------
    // Timestamps
    // ---------------------------------------------------------------------

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    // ---------------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------------

    protected OutboxEventEntity() {
        // JPA
    }

    private OutboxEventEntity(
            UUID id,
            String aggregateType,
            String aggregateId,
            String eventType,
            String eventVersion,
            String tenantId,
            String traceId,
            Instant occurredAt,
            String payload,
            String headersJson
    ) {
        this.id = Objects.requireNonNull(id);
        this.aggregateType = Objects.requireNonNull(aggregateType);
        this.aggregateId = Objects.requireNonNull(aggregateId);
        this.eventType = Objects.requireNonNull(eventType);
        this.eventVersion = Objects.requireNonNull(eventVersion);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.traceId = Objects.requireNonNull(traceId);
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.payload = Objects.requireNonNull(payload);
        this.headersJson = headersJson;

        this.status = OutboxEventStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = Instant.now();
    }

    // ---------------------------------------------------------------------
    // Factory
    // ---------------------------------------------------------------------

    public static OutboxEventEntity pending(
            String aggregateType,
            String aggregateId,
            String eventType,
            String eventVersion,
            String tenantId,
            String traceId,
            Instant occurredAt,
            String payload,
            String headersJson
    ) {
        return new OutboxEventEntity(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                eventType,
                eventVersion,
                tenantId,
                traceId,
                occurredAt,
                payload,
                headersJson
        );
    }

    // ---------------------------------------------------------------------
    // State transitions
    // ---------------------------------------------------------------------

    public void markPublished() {
        this.status = OutboxEventStatus.PUBLISHED;
        this.publishedAt = Instant.now();
    }

    public void markFailed() {
        this.status = OutboxEventStatus.FAILED;
        this.retryCount++;
        this.lastAttemptAt = Instant.now();
    }

    // ---------------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------------

    public UUID id() { return id; }
    public String aggregateType() { return aggregateType; }
    public String aggregateId() { return aggregateId; }
    public String eventType() { return eventType; }
    public String eventVersion() { return eventVersion; }
    public String tenantId() { return tenantId; }
    public String traceId() { return traceId; }
    public Instant occurredAt() { return occurredAt; }
    public String payload() { return payload; }
    public String headersJson() { return headersJson; }
    public OutboxEventStatus status() { return status; }
    public int retryCount() { return retryCount; }
    public Instant createdAt() { return createdAt; }
    public Instant lastAttemptAt() { return lastAttemptAt; }
    public Instant publishedAt() { return publishedAt; }
    public Instant nextRetryAt() { return nextRetryAt; }
}
