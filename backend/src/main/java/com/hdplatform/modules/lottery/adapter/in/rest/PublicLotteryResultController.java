package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.service.AnalyticsTracker;
import com.hdplatform.modules.lottery.application.service.LotteryResultService;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/lottery/results")
public class PublicLotteryResultController {
    private final LotteryResultService service;
    private final AnalyticsTracker analyticsTracker;

    @GetMapping
    public ApiResponse<List<LotteryResultResponse>> published(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam LotteryRegion region) {
        List<LotteryResultResponse> results = service.published(date, region).stream()
                .map(LotteryResultResponse::from).toList();
        analyticsTracker.recordBestEffort(
                TenantContextHolder.requireCurrent().tenantId(), AnalyticsEvents.LOTTERY_RESULT_VIEW);
        return ApiResponse.success(results);
    }

    @GetMapping("/live")
    public ApiResponse<List<LotteryResultResponse>> live(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam LotteryRegion region) {
        List<LotteryResultResponse> results = service.live(date, region).stream()
                .map(LotteryResultResponse::from).toList();
        analyticsTracker.recordBestEffort(
                TenantContextHolder.requireCurrent().tenantId(), AnalyticsEvents.LOTTERY_RESULT_VIEW);
        return ApiResponse.success(results);
    }
}
