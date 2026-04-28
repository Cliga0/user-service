package com.solvia.userservice.application.port.out.auth;

import com.solvia.userservice.application.security.context.AuthenticatedUser;

import java.util.Optional;

public interface AuthenticatedUserProvider {
    Optional<AuthenticatedUser> current();
}
