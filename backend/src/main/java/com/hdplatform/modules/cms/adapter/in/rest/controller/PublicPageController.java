package com.hdplatform.modules.cms.adapter.in.rest.controller;

import com.hdplatform.modules.cms.adapter.in.rest.response.PageResponse;
import com.hdplatform.modules.cms.application.service.PageService;
import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.service.AnalyticsTracker;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/cms/pages")
public class PublicPageController {
    private final PageService pageService;
    private final AnalyticsTracker analyticsTracker;

    @GetMapping("/{slug}")
    public ApiResponse<PageResponse> getPublishedBySlug(@PathVariable String slug) {
        Page page = pageService.getPublishedBySlug(slug);
        analyticsTracker.recordBestEffort(
                TenantContextHolder.requireCurrent().tenantId(), AnalyticsEvents.PAGE_VIEW);
        return ApiResponse.success(PageResponse.from(page));
    }
}
