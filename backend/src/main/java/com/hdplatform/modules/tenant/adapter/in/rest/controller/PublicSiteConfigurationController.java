package com.hdplatform.modules.tenant.adapter.in.rest.controller;

import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.application.query.SiteConfiguration;
import com.hdplatform.modules.tenant.application.service.SiteConfigurationService;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/site")
public class PublicSiteConfigurationController {
    private final SiteConfigurationService service;

    @GetMapping
    @TenantRequired
    public ApiResponse<SiteConfiguration> current() {
        var context = TenantContextHolder.requireCurrent();
        return ApiResponse.success(service.resolve(context.tenantId()));
    }
}
