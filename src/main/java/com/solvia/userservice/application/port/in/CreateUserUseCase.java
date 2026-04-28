package com.solvia.userservice.application.port.in;

import com.solvia.userservice.application.command.model.CreateUserCommand;
import com.solvia.userservice.application.command.result.CreateUserResult;

/**
 * CreateUserUseCase
 * <p>
 * APPLICATION PORT (INBOUND)
 * <p>
 * RESPONSIBILITY:
 * - define the contract for creating a user
 * <p>
 * NON RESPONSIBILITIES:
 * - no orchestration logic
 * - no infrastructure concern
 * - no validation implementation
 */
@FunctionalInterface
public interface CreateUserUseCase {

    /**
     * Execute the creation user use case
     *
     * @param command immutable intent object
     * @return result of the operation
     */
    CreateUserResult createUser(CreateUserCommand command);
}