package com.solvia.userservice.application.command.handler;

import com.solvia.userservice.application.command.model.CreateUserCommand;
import com.solvia.userservice.application.command.result.CreateUserResult;
import com.solvia.userservice.application.command.result.CreateUserResultMapper;
import com.solvia.userservice.application.exception.UnauthenticatedException;
import com.solvia.userservice.application.exception.UserAlreadyExistsException;
import com.solvia.userservice.application.port.in.CreateUserUseCase;
import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.application.security.intent.AuthorizationIntent;
import com.solvia.userservice.application.security.service.AuthorizationAppService;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.factory.UserFactory;
import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.contact.*;
import com.solvia.userservice.domain.model.vo.identity.*;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.person.*;

import java.util.Objects;

/**
 * CreateUserHandler
 * <p>
 * APPLICATION SERVICE (CQRS COMMAND HANDLER)
 * <p>
 * RESPONSABILITÉS :
 * - Orchestration du use case
 * - Gestion de l’idempotence
 * - Appel Factory (construction domaine)
 * - Persistance via repository
 * - Publication des événements
 * <p>
 * ❌ Aucune logique métier
 * ❌ Aucun invariant
 * ❌ Aucun mapping complexe
 * <p>
 * ✔ Stateless
 * ✔ Transaction boundary candidate
 * ✔ Clean Architecture compliant
 */
public final class CreateUserHandler implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final UserFactory userFactory;
    private final AuthenticatedUserProvider authUserProvider;
    private final AuthorizationAppService authorizationService;
    private final CreateUserResultMapper resultMapper;

    public CreateUserHandler(
            UserRepository userRepository,
            EventPublisher eventPublisher,
            UserFactory userFactory,
            AuthenticatedUserProvider authUserProvider,
            AuthorizationAppService authorizationService,
            CreateUserResultMapper resultMapper
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.userFactory = Objects.requireNonNull(userFactory);
        this.authUserProvider = Objects.requireNonNull(authUserProvider);
        this.authorizationService = Objects.requireNonNull(authorizationService);
        this.resultMapper = Objects.requireNonNull(resultMapper);
    }

    /**
     * Exécute le use case de création d’utilisateur.
     */
    public CreateUserResult createUser(CreateUserCommand command) {

        Objects.requireNonNull(command, "CreateUserCommand must not be null");

        // =========================
        // 1. RÉCUPÉRER USER AUTHENTIFIÉ
        // =========================

        AuthenticatedUser authUser = authUserProvider.current()
                .orElseThrow(UnauthenticatedException::new);

        // =========================
        // 2. AUTHORIZATION (Zanzibar style)
        // =========================

        authorizationService.check(
                authUser,
                AuthorizationIntent.USER_CREATE,
                AuthorizationIntent.system()
        );

        // =========================
        // 3. ID GENERATION / NORMALIZATION
        // =========================

        ActorId actorId = ActorId.of(authUser.userId());

        UserId userId = (command.userId() != null)
                ? UserId.of(command.userId())
                : UserId.generate();

        // =========================
        // 4. IDEMPOTENCY CHECK
        // =========================

        ensureUserDoesNotExist(userId);

        // =========================
        // 5. DOMAIN CREATION (delegated)
        // =========================

        User user = userFactory.create(
                userId,
                ExternalAuthId.of(command.externalAuthId()),
                FirstName.of(command.firstName()),
                LastName.of(command.lastName()),
                BirthDate.of(command.birthDate()),
                Gender.fromString(command.gender()),
                Email.of(command.email()),
                PhoneNumber.of(command.phoneNumber()),
                UserRole.of(UserRole.Code.from(command.role())),
                actorId
        );

        // =========================
        // 6. PERSISTENCE
        // =========================

        userRepository.save(user);

        // =========================
        // 7. DOMAIN EVENTS PUBLICATION
        // =========================

        publishDomainEvents(user);

        // =========================
        // 8. RETURN RESULT
        // =========================

        return resultMapper.map(user);
    }

    // =====================================================
    // PRIVATE ORCHESTRATION HELPERS (NO BUSINESS LOGIC)
    // =====================================================

    private void ensureUserDoesNotExist(UserId userId) {
        if (userRepository.findById(userId).isPresent()) {
            throw new UserAlreadyExistsException(userId);
        }
    }

    private void publishDomainEvents(User user) {
        eventPublisher.publish(user.pullEvents());
    }
}
