package com.hdplatform.modules.identity.application.query;

import java.util.List;
import java.util.UUID;

public record RoleSummary(
        UUID id,
        String scope,
        UUID tenantId,
        String code,
        String name,
        List<String> permissions
) {
    public RoleSummary {
        permissions = List.copyOf(permissions);
    }
}
