package com.hdplatform.modules.tenant.adapter.in.rest.controller;

import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import com.hdplatform.modules.tenant.adapter.in.rest.mapper.TenantRestMapper;
import com.hdplatform.modules.tenant.adapter.in.rest.response.TenantResponse;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.application.usecase.CreateTenantService;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/site/settings")
@TenantRequired
public class TenantSiteSettingsController {
    private final CreateTenantService tenants;
    private final TenantRestMapper mapper;

    @PutMapping("/template")
    @PreAuthorize(AuthorizationExpressions.TENANT_TEMPLATE_SWITCH)
    public ApiResponse<TenantResponse> switchTemplate(@Valid @RequestBody SwitchTemplateRequest request) {
        var tenantId = TenantContextHolder.requireCurrent().tenantId();
        return ApiResponse.success(mapper.toResponse(
                tenants.switchTemplate(tenantId, TemplateId.of(request.templateId()))));
    }

    public record SwitchTemplateRequest(@NotNull UUID templateId) {}
}
