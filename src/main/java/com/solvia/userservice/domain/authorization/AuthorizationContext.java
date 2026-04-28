package com.solvia.userservice.domain.authorization;

import com.solvia.userservice.shared.TenantId;

public record AuthorizationContext(
        String subject,
        TenantId tenantId
) {}