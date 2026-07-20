package com.hdplatform.modules.reporting.adapter.in.rest;

import com.hdplatform.modules.reporting.application.service.TenantMetrics;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reporting")
@PreAuthorize(AuthorizationExpressions.REPORTING_DASHBOARD_READ)
public class ReportingController {
    private final TenantMetrics tenantMetrics;

    @GetMapping("/overview")
    public ApiResponse<Map<String, Long>> overview() {
        return ApiResponse.success(tenantMetrics.currentTenantSnapshot());
    }
}
