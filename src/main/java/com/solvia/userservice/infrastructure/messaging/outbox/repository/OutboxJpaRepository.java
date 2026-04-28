package com.solvia.userservice.infrastructure.messaging.outbox.repository;

import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

interface OutboxJpaRepository extends JpaRepository<OutboxEventEntity, UUID> {

    // ---------------------------------------------------------------------
    // FETCH WITH SKIP LOCKED (CRITICAL)
    // ---------------------------------------------------------------------

    @Query(
            value = """
            SELECT *
            FROM outbox_event
            WHERE status = 'PENDING'
              AND (next_retry_at IS NULL OR next_retry_at <= :now)
            ORDER BY created_at
            LIMIT :batchSize
            FOR UPDATE SKIP LOCKED
            """,
            nativeQuery = true
    )
    List<OutboxEventEntity> fetchPendingBatchForUpdate(
            @Param("batchSize") int batchSize,
            @Param("now") Instant now
    );

    // ---------------------------------------------------------------------
    // STATE TRANSITIONS (ATOMIC)
    // ---------------------------------------------------------------------

    @Modifying
    @Query("""
        UPDATE OutboxEventEntity e
        SET e.status = 'PUBLISHED',
            e.publishedAt = :publishedAt
        WHERE e.id = :eventId
    """)
    int markAsPublished(
            @Param("eventId") UUID eventId,
            @Param("publishedAt") Instant publishedAt
    );

    @Modifying
    @Query("""
        UPDATE OutboxEventEntity e
        SET e.status = 'FAILED',
            e.retryCount = e.retryCount + 1,
            e.nextRetryAt = :nextRetryAt
        WHERE e.id = :eventId
    """)
    int markAsFailed(
            @Param("eventId") UUID eventId,
            @Param("nextRetryAt") Instant nextRetryAt
    );
}
