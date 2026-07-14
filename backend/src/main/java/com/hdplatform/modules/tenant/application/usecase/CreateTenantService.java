package com.hdplatform.modules.tenant.application.usecase;

import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.application.command.UpdateTenantCommand;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.application.query.GetTenantQuery;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.exception.NotFoundException;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
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
            throw new DomainException(
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

    @Override
    public Tenant getById(GetTenantQuery query) {

        return repository.findById(query.tenantId())
                .orElseThrow(() ->
                        new NotFoundException(HttpStatus.NOT_FOUND.toString(),"Tenant not found"));

    }

    @Override
    public List<Tenant> getAll() {
       return repository.findAll();
    }

    @Override
    public Tenant update(TenantId id, UpdateTenantCommand command) {
        Tenant tenant = repository.findById(id)
        .orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND.toString(),"Tenant not found"));
        tenant.changeDisplayName(command.displayName());
        tenant.changeLogo(command.logo());
        tenant.changeHotline(command.hotline());
        return repository.save(tenant);
    }

    @Override
    public void delete(TenantId id) {
        Tenant tenant = repository.findById(id)
        .orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND.toString(),"Tenant not found"));

        tenant.archive(Instant.now());
        repository.save(tenant);
    }

}