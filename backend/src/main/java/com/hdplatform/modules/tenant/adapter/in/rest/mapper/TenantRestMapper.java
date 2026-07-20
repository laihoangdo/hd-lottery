package com.hdplatform.modules.tenant.adapter.in.rest.mapper;

import org.springframework.stereotype.Component;

import com.hdplatform.modules.tenant.adapter.in.rest.request.CreateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.request.UpdateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.response.TenantResponse;
import com.hdplatform.modules.tenant.application.command.CreateTenantCommand;
import com.hdplatform.modules.tenant.application.command.UpdateTenantCommand;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import com.hdplatform.modules.tenant.domain.valueobject.LogoUrl;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantName;
import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import com.hdplatform.modules.platformcatalog.domain.VerticalId;

@Component
public class TenantRestMapper {

    public CreateTenantCommand toCommand(
            CreateTenantRequest request
    ) {

        return new CreateTenantCommand(

                TenantId.newId(),
                TenantName.of(request.name()),
                TenantCode.of(request.code()),
                SiteKey.of(request.siteKey()),
                DomainName.of(request.domainName()),

                DisplayName.of(request.displayName()),
              

                request.logoUrl() == null
                        ? null
                        : LogoUrl.of(request.logoUrl()),

                request.hotline() == null
                        ? null
                        : Hotline.of(request.hotline()),
                        VerticalId.of(request.verticalId()),
                        TemplateId.of(request.templateId()),
                        request.ownerEmail(),
                        request.ownerFullName(),
                        request.ownerInitialPassword(),
                        null,
                        null,
                        null

        );

    }

    public UpdateTenantCommand toUpdateCommand(
        UpdateTenantRequest request
) {

    return new UpdateTenantCommand(
           DisplayName.of(request.displayName()),
           request.logoUrl() == null
                    ? null
                    : LogoUrl.of(request.logoUrl()),

            request.hotline() == null
                    ? null
                    : Hotline.of(request.hotline())
    );

}

    public TenantResponse toResponse(
            Tenant tenant
    ) {

        return new TenantResponse(

                tenant.getId().getValue(),

                tenant.getSiteKey().value(),

                tenant.getDomainName().value(),

                tenant.getDisplayName().value(),

                tenant.getLogoUrl() == null
                        ? null
                        : tenant.getLogoUrl().value().toString(),

                tenant.getHotline() == null
                        ? null
                        : tenant.getHotline().value(),

                tenant.getStatus().name(),

                tenant.getVerticalId().getValue(),

                tenant.getTemplateId().getValue(),

                tenant.getCreatedAt(),

                tenant.getUpdatedAt()
        );

    }

}
