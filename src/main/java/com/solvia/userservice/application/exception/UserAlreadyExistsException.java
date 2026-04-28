package com.solvia.userservice.application.exception;

import com.solvia.userservice.domain.model.vo.identity.UserId;

public class UserAlreadyExistsException extends RuntimeException {

    private final UserId userId;

    public UserAlreadyExistsException(UserId userId) {
        super("User already exists: " + userId);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
