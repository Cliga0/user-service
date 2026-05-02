package com.solvia.userservice.application.port.out;

import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.identity.UserId;

import java.util.Optional;

/**
 * UserRepository
 * <p>
 * Port du domaine pour la persistance des Users.
 * <p>
 * ⚠️ CLEAN ARCHITECTURE:
 * - aucune dépendance infrastructure
 * - uniquement langage métier
 */
public interface UserRepository {

    /**
     * Sauvegarde ou met à jour un User aggregate.
     */
    User save(User user);

    /**
     * Récupère un User par son identifiant métier.
     */
    Optional<User> findById(UserId id);


    Optional<User> findByEmail(Email email);

    /**
     * Vérifie l’existence d’un User.
     */
    boolean existsById(UserId id);

    /**
     * Suppression logique ou physique selon infra.
     */
    void delete(User user);
}
