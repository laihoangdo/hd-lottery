package com.hdplatform.modules.tenant.application.query;

import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

public record GetTenantQuery(
        TenantId tenantId
) {
}