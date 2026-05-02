package com.solvia.userservice.application.command.signup.handler;

import com.solvia.userservice.application.command.signup.model.SignupUserCommand;
import com.solvia.userservice.application.command.signup.result.SignupUserResult;
import com.solvia.userservice.application.command.signup.result.SignupUserResultMapper;
import com.solvia.userservice.application.exception.EmailAlreadyUsedException;
import com.solvia.userservice.application.port.in.SignupUserUseCase;
import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.factory.UserFactory;
import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.contact.*;
import com.solvia.userservice.domain.model.vo.identity.*;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.person.*;

import java.util.Objects;

/**
 * APPLICATION SERVICE (DDD / CQRS STYLE)
 * <p>
 * RESPONSABILITIES:
 * - Orchestration du use case Signup
 * - Coordination domaine + persistence + events
 * - Garantir idempotence fonctionnelle (email unique)
 * <p>
 * NON-RESPONSIBILITIES:
 * - Aucune logique métier riche (domain owns it)
 * - Aucune règle métier complexe (domain layer)
 * - Aucun mapping HTTP / infrastructure
 * <p>
 * DESIGN:
 * - Stateless
 * - Side-effect explicit
 * - Testable in isolation
 * - Ready for event-driven evolution
 */
public final class SignupUserHandler implements SignupUserUseCase {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final UserFactory userFactory;
    private final SignupUserResultMapper resultMapper;

    public SignupUserHandler(
            UserRepository userRepository,
            EventPublisher eventPublisher,
            UserFactory userFactory,
            SignupUserResultMapper resultMapper
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.userFactory = Objects.requireNonNull(userFactory);
        this.resultMapper = Objects.requireNonNull(resultMapper);
    }

    /**
     * Entry point of Signup use case
     */
    public SignupUserResult signup (SignupUserCommand command) {

        Objects.requireNonNull(command, "SignupUserCommand must not be null");

        // =====================================================
        // 1. VALUE OBJECTS CREATION (fail fast)
        // =====================================================
        ExternalAuthId externalAuthId = ExternalAuthId.of(command.externalAuthId());

        FirstName firstName = FirstName.of(command.firstName());

        LastName lastName = LastName.of(command.lastName());

        BirthDate birthDate = BirthDate.of(command.birthDate());

        Gender gender = Gender.fromString(command.gender());

        Email email = Email.of(command.email());

        PhoneNumber phoneNumber = PhoneNumber.of(command.phoneNumber());

        // =====================================================
        // 2. BUSINESS RULE: EMAIL UNIQUENESS
        // =====================================================

        ensureEmailIsAvailable(email);

        // =====================================================
        // 3. SYSTEM ACTOR (signup = unauthenticated context)
        // =====================================================

        ActorId actorId = ActorId.system();

        // =====================================================
        // 4. DOMAIN AGGREGATE CREATION (factory owns invariants)
        // =====================================================

        User user = userFactory.create(
                UserId.generate(),
                externalAuthId,
                firstName,
                lastName,
                birthDate,
                gender,
                email,
                phoneNumber,
                UserRole.of(UserRole.Code.CUSTOMER),
                actorId
        );

        // =====================================================
        // 5. PERSISTENCE (OUTBOUND PORT)
        // =====================================================

        userRepository.save(user);

        // =====================================================
        // 6. DOMAIN EVENTS (EVENT-DRIVEN READY)
        // =====================================================

        eventPublisher.publish(user.pullEvents());

        // =====================================================
        // 7. RESULT MAPPING (READ MODEL)
        // =====================================================

        return resultMapper.map(user);
    }

    // =========================================================
    // PRIVATE DOMAIN-LEVEL GUARDS (application concern)
    // =========================================================

    private void ensureEmailIsAvailable(Email email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException(email.asString());
        }
    }
}