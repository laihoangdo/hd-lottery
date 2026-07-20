package com.hdplatform.modules.tenant.application.port;

import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

public interface TenantIdentityProvisioner {
    void provisionDefaultRoles(TenantId tenantId);
    void provisionWebsiteOwner(TenantId tenantId, TenantOwnerAccount owner);
}
