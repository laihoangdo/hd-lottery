package com.hdplatform.modules.tenant.application.context;

import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;

import java.util.Objects;

/** Immutable tenant identity attached to one unit of work. */
public record TenantContext(TenantId tenantId, SiteKey siteKey) {

    public TenantContext {
        Objects.requireNonNull(tenantId, "tenantId cannot be null");
        Objects.requireNonNull(siteKey, "siteKey cannot be null");
    }
}
