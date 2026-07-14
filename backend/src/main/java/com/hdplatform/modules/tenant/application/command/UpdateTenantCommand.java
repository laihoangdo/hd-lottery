package com.hdplatform.modules.tenant.application.command;

import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import com.hdplatform.modules.tenant.domain.valueobject.LogoUrl;

public record UpdateTenantCommand(
    DisplayName displayName,
    LogoUrl logo,
    Hotline hotline
) {
}
