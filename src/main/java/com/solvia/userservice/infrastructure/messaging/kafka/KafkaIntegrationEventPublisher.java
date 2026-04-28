package com.solvia.userservice.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvia.userservice.shared.events.IntegrationEvent;
import com.solvia.userservice.application.port.out.event.IntegrationEventPublisher;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;

/**
 * KafkaIntegrationEventPublisher
 *
 * <p>
 * Adapter infrastructure responsable de la publication
 * des IntegrationEvents vers Kafka.
 *
 * <p>
 * - Stateless
 * - Thread-safe
 * - Tolérant aux erreurs
 * - Compatible Outbox Pattern
 */
public class KafkaIntegrationEventPublisher implements IntegrationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaIntegrationEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topicName = "product-catalog.events";

    public KafkaIntegrationEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // ---------------------------------------------------------------------
    // PUBLISH
    // ---------------------------------------------------------------------

    @Override
    public void publish(IntegrationEvent event) {
        try {
            String payload = serialize(event);

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(
                            topicName,
                        event.aggregateId(),
                            payload
                    );

            enrichHeaders(record, event);

            kafkaTemplate.send(record);

            log.debug(
                    "IntegrationEvent published to Kafka | type={} | aggregateId={} | eventId={}",
                    event.eventType(),
                    event.aggregateId(),
                    event.eventId()
            );

        } catch (Exception ex) {
            log.error(
                    "Failed to publish IntegrationEvent to Kafka | eventId={} | type={}",
                    event.eventId(),
                    event.eventType(),
                    ex
            );

            // On remonte l'exception :
            // 👉 l'OutboxPublisherJob décidera retry / fail
            throw new IntegrationEventPublishingException(event, ex);
        }
    }

    // ---------------------------------------------------------------------
    // INTERNALS
    // ---------------------------------------------------------------------

    private String serialize(IntegrationEvent event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    private void enrichHeaders(ProducerRecord<String, String> record, IntegrationEvent event) {
        record.headers().add("event_id",
                event.eventId().toString().getBytes(StandardCharsets.UTF_8));

        record.headers().add("event_type",
                event.eventType().getBytes(StandardCharsets.UTF_8));

        record.headers().add("tenant_id",
                event.tenantId().getBytes(StandardCharsets.UTF_8));

        record.headers().add("trace_id",
                event.traceId().getBytes(StandardCharsets.UTF_8));

        record.headers().add("occurred_at",
                event.occurredAt().toString().getBytes(StandardCharsets.UTF_8));
    }
}
