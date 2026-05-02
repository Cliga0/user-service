package com.solvia.userservice.application.port.in;

import com.solvia.userservice.application.command.signup.model.SignupUserCommand;
import com.solvia.userservice.application.command.signup.result.SignupUserResult;

public interface SignupUserUseCase {
    SignupUserResult signup(SignupUserCommand command);
}
