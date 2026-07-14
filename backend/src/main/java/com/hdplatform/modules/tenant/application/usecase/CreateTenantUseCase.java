package com.hdplatform.modules.tenant.application.usecase;

import java.util.List;

import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.application.command.UpdateTenantCommand;
import com.hdplatform.modules.tenant.application.query.GetTenantQuery;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

public interface CreateTenantUseCase {

    Tenant execute(CreateTenantCommand command);
    Tenant getById(GetTenantQuery query);
    List<Tenant> getAll();
    Tenant update(TenantId id,UpdateTenantCommand command);
    void delete(TenantId id);

}