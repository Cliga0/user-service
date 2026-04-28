package com.solvia.userservice.application.port.out.event;

import com.solvia.userservice.shared.events.IntegrationEvent;

/**
 * IntegrationEventPublisher
 *
 * <p>
 * Port applicatif – publication d’événements d’intégration.
 *
 * <p>
 * Responsabilités :
 * <ul>
 *   <li>Publier un événement vers un système externe</li>
 *   <li>Garantir l’envoi at-least-once</li>
 * </ul>
 *
 * <p>
 * Contraintes :
 * <ul>
 *   <li>Pas de dépendance technologique</li>
 *   <li>Stateless</li>
 *   <li>Idempotent côté consommateur</li>
 * </ul>
 */
public interface IntegrationEventPublisher {

    /**
     * Publie un événement d’intégration.
     *
     * @param event événement à publier (non null)
     * @throws Exception en cas d’échec technique
     */
    void publish(IntegrationEvent event) throws Exception;
}
