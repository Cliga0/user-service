package com.solvia.userservice.application.security.mapper;

import com.solvia.userservice.application.security.context.SecurityRole;
import com.solvia.userservice.domain.model.vo.business.UserRole;

public final class UserRoleMapper {

    private UserRoleMapper() {}

    public static UserRole toDomain(SecurityRole role) {
        return UserRole.of(
                UserRole.Code.from(role.value())
        );
    }
}