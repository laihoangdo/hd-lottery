package com.hdplatform.modules.tenant.application.usecase;

import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import com.hdplatform.shared.domain.ClockProvider;
import org.springframework.stereotype.Service;

@Service
public class CreateTenantService
        implements CreateTenantUseCase {

    private final TenantRepository repository;

    private final ClockProvider clockProvider;

    public CreateTenantService(
            TenantRepository repository,
            ClockProvider clockProvider
    ) {

        this.repository = repository;
        this.clockProvider = clockProvider;
    }

    @Override
    public Tenant execute(CreateTenantCommand command) {

        TenantCode code = TenantCode.of(command.code().value());

        if (repository.existsByCode(code)) {
            throw new IllegalArgumentException(
                    "Tenant code already exists.");
        }

        Tenant tenant =
        Tenant.register(
            // idGenerator.next(),
            command.id(),
            command.siteKey(),
            command.name(),
            command.code(),
            command.domainName(),
            command.displayName(),
            command.logo(),
            command.hotline(),
            TenantStatus.ACTIVE,
            clockProvider.now()
    );

        return repository.save(tenant);
    }

}