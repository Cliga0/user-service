package com.solvia.userservice.infrastructure.messaging.outbox.repository;

import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository {

    /**
     * Sauvegarde un événement d’outbox.
     */
    void save(OutboxEventEntity event);

    /**
     * Récupère un batch d’événements prêts à être publiés.
     *
     * <p>
     * Doit être safe en concurrence multi-instance.
     */
    List<OutboxEventEntity> fetchPendingBatch(int batchSize);

    /**
     * Marque un événement comme publié avec succès.
     */
    void markAsPublished(UUID eventId);

    /**
     * Marque un événement comme échoué (retry).
     */
    void markAsFailed(UUID eventId);
}
