package com.solvia.userservice.infrastructure.messaging.outbox.repository;

import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@Profile("!test")
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository jpaRepository;

    public OutboxRepositoryImpl(OutboxJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ---------------------------------------------------------------------
    // SAVE
    // ---------------------------------------------------------------------

    @Override
    @Transactional
    public void save(OutboxEventEntity event) {
        jpaRepository.save(event);
    }

    // ---------------------------------------------------------------------
    // FETCH
    // ---------------------------------------------------------------------

    @Override
    @Transactional
    public List<OutboxEventEntity> fetchPendingBatch(int batchSize) {
        Instant now = Instant.now();
        return jpaRepository.fetchPendingBatchForUpdate(batchSize, now);
    }

    // ---------------------------------------------------------------------
    // STATE TRANSITIONS
    // ---------------------------------------------------------------------

    @Override
    @Transactional
    public void markAsPublished(UUID eventId) {
        int updated = jpaRepository.markAsPublished(
            eventId,
            Instant.now()
        );

        if (updated == 0) {
            throw new IllegalStateException(
                "Outbox event not found or already processed: " + eventId
            );
        }
    }

    @Override
    @Transactional
    public void markAsFailed(UUID eventId) {
        Instant nextRetry = Instant.now().plusSeconds(30);

        int updated = jpaRepository.markAsFailed(
            eventId,
            nextRetry
        );

        if (updated == 0) {
            throw new IllegalStateException(
                "Outbox event not found or already processed: " + eventId
            );
        }
    }
}
