package com.hdplatform.modules.tenant.application.usecase;

import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;

public interface CreateTenantUseCase {

    Tenant execute(CreateTenantCommand command);

}