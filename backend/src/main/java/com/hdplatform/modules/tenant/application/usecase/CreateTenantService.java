package com.hdplatform.modules.tenant.application.usecase;

import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.application.command.UpdateTenantCommand;
import com.hdplatform.modules.tenant.application.port.TenantRepository;
import com.hdplatform.modules.tenant.application.port.TenantIdentityProvisioner;
import com.hdplatform.modules.tenant.application.port.TenantOwnerAccount;
import com.hdplatform.modules.tenant.application.query.GetTenantQuery;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.exception.NotFoundException;
import com.hdplatform.modules.platformcatalog.application.PlatformCatalogRepository;
import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import com.hdplatform.modules.platformcatalog.domain.VerticalId;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTenantService
        implements CreateTenantUseCase {

    private final TenantRepository repository;

    private final ClockProvider clockProvider;
    private final PlatformCatalogRepository catalogRepository;
    private final TenantIdentityProvisioner identityProvisioner;

    public CreateTenantService(
            TenantRepository repository,
            ClockProvider clockProvider,
            PlatformCatalogRepository catalogRepository,
            TenantIdentityProvisioner identityProvisioner
    ) {

        this.repository = repository;
        this.clockProvider = clockProvider;
        this.catalogRepository = catalogRepository;
        this.identityProvisioner = identityProvisioner;
    }

    @Override
    @Transactional
    public Tenant execute(CreateTenantCommand command) {

        validateTemplate(command.verticalId(), command.templateId());

        TenantCode code = TenantCode.of(command.code().value());

        if (repository.existsByCode(code)) {
            throw new DomainException(
                    "Tenant code already exists.");
        }

        if (repository.existsBySiteKey(command.siteKey())) {
            throw new DomainException("Site key already exists.");
        }

        if (repository.existsByDomainName(command.domainName())) {
            throw new DomainException("Domain name already exists.");
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
            command.verticalId(),
            command.templateId(),
            clockProvider.now()
    );

        Tenant saved = repository.save(tenant);
        identityProvisioner.provisionDefaultRoles(saved.getId());
        identityProvisioner.provisionWebsiteOwner(saved.getId(), new TenantOwnerAccount(
                command.ownerEmail(), command.ownerFullName(), command.ownerInitialPassword()));
        return saved;
    }

    public Tenant switchTemplate(TenantId tenantId, TemplateId templateId) {
        Tenant tenant = repository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.toString(), "Tenant not found"));
        validateTemplate(tenant.getVerticalId(), templateId);
        tenant.switchTemplate(templateId, clockProvider.now());
        return repository.save(tenant);
    }

    private void validateTemplate(VerticalId verticalId, TemplateId templateId) {
        var vertical = catalogRepository.findVertical(verticalId)
                .orElseThrow(() -> new DomainException("Vertical not found"));
        if (!vertical.isActive()) throw new DomainException("Vertical is inactive");
        var template = catalogRepository.findTemplate(templateId)
                .orElseThrow(() -> new DomainException("Template not found"));
        if (!template.isActive()) throw new DomainException("Template is inactive");
        if (!template.getVerticalId().equals(verticalId)) {
            throw new DomainException("Template does not belong to the selected vertical");
        }
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
