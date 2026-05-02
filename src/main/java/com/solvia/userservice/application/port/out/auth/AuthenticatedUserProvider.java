package com.solvia.userservice.application.port.out.auth;

import com.solvia.userservice.application.security.context.AuthenticatedUser;

public interface AuthenticatedUserProvider {
    AuthenticatedUser current();
}
