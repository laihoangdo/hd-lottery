package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.service.AnalyticsTracker;
import com.hdplatform.modules.lottery.application.command.CheckTicketCommand;
import com.hdplatform.modules.lottery.application.service.TicketCheckService;
import com.hdplatform.modules.lottery.domain.service.TicketCheckResult;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/lottery/tickets")
public class PublicTicketCheckController {
    private final TicketCheckService service;
    private final AnalyticsTracker analyticsTracker;

    @PostMapping("/check")
    public ApiResponse<TicketCheckResponse> check(
            @Valid @RequestBody CheckTicketRequest request) {
        TicketCheckResult result = service.check(new CheckTicketCommand(
                request.provinceCode(), request.drawDate(), request.ticketNumber()));
        analyticsTracker.recordBestEffort(
                TenantContextHolder.requireCurrent().tenantId(),
                AnalyticsEvents.LOTTERY_TICKET_CHECK);
        return ApiResponse.success(TicketCheckResponse.from(result));
    }
}
