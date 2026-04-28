package com.solvia.userservice.application.query.exception;

import com.solvia.userservice.domain.model.vo.identity.UserId;

public final class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UserId userId) {
        super("User not found: " + userId.asString());
    }
}