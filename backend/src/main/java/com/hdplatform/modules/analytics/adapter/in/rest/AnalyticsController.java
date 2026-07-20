package com.hdplatform.modules.analytics.adapter.in.rest;

import com.hdplatform.modules.analytics.application.query.DailyEventCount;
import com.hdplatform.modules.analytics.application.service.AnalyticsQueryService;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/analytics")
@PreAuthorize(AuthorizationExpressions.ANALYTICS_DASHBOARD_READ)
public class AnalyticsController {
    private final AnalyticsQueryService queryService;

    @GetMapping("/trend")
    public ApiResponse<List<DailyEventCount>> trend(
            @RequestParam String event,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(queryService.trend(event, from, to));
    }
}
