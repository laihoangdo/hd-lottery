package com.hdplatform.modules.tenant.application.command;

import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;   
import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.TenantName;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.LogoUrl;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import java.time.Instant;

public record CreateTenantCommand(
        TenantId id,
        TenantName name,
        TenantCode code,
        SiteKey siteKey,
        DomainName domainName,
        DisplayName displayName,
        LogoUrl logo,
        Hotline hotline,
        TenantStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}