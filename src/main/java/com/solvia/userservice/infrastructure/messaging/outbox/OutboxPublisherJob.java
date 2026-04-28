package com.solvia.userservice.infrastructure.messaging.outbox;

import com.solvia.userservice.application.port.out.event.IntegrationEventPublisher;
import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;
import com.solvia.userservice.infrastructure.messaging.outbox.repository.OutboxRepository;
import com.solvia.userservice.shared.events.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

public class OutboxPublisherJob {

    private static final Logger log =
            LoggerFactory.getLogger(OutboxPublisherJob.class);

    private final OutboxRepository outboxRepository;
    private final IntegrationEventPublisher publisher;

    private final int batchSize = 50;

    public OutboxPublisherJob(
            OutboxRepository outboxRepository,
            IntegrationEventPublisher publisher
    ) {
        this.outboxRepository = outboxRepository;
        this.publisher = publisher;
    }

    // ---------------------------------------------------------------------
    // SCHEDULER
    // ---------------------------------------------------------------------

    @Scheduled(
            fixedDelayString = "${outbox.publisher.delay-ms:500}"
    )
    public void publishPendingEvents() {

        List<OutboxEventEntity> events =
                outboxRepository.fetchPendingBatch(batchSize);

        if (events.isEmpty()) {
            return;
        }

        log.debug("Publishing {} outbox events", events.size());

        for (OutboxEventEntity event : events) {
            publishSingle(event);
        }
    }

    // ---------------------------------------------------------------------
    // SINGLE EVENT
    // ---------------------------------------------------------------------

    private void publishSingle(OutboxEventEntity entity) {
        try {

            IntegrationEvent event = mapToIntegrationEvent(entity);

            publisher.publish(event);

            outboxRepository.markAsPublished(entity.id());

            log.info(
                    "Outbox event {} published successfully",
                    entity.id()
            );

        } catch (Exception ex) {

            outboxRepository.markAsFailed(entity.id());

            log.error(
                    "Failed to publish outbox event {}",
                    entity.id(),
                    ex
            );
        }
    }

    // ---------------------------------------------------------------------
    // MAPPING
    // ---------------------------------------------------------------------

    private IntegrationEvent mapToIntegrationEvent(OutboxEventEntity entity) {

        return new IntegrationEvent(
                entity.id(),
                entity.eventType(),
                entity.aggregateId(),
                entity.aggregateType(),
                entity.aggregateType(),
                entity.payload(),
                entity.createdAt(),
                Map.of()
        );

    }
}
