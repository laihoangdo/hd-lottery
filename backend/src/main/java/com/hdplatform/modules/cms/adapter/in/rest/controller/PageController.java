package com.hdplatform.modules.cms.adapter.in.rest.controller;

import com.hdplatform.modules.cms.adapter.in.rest.request.CreatePageRequest;
import com.hdplatform.modules.cms.adapter.in.rest.request.UpdatePageRequest;
import com.hdplatform.modules.cms.adapter.in.rest.response.PageResponse;
import com.hdplatform.modules.cms.application.command.CreatePageCommand;
import com.hdplatform.modules.cms.application.command.UpdatePageCommand;
import com.hdplatform.modules.cms.domain.aggregate.PageId;
import com.hdplatform.modules.cms.application.service.PageService;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/cms/pages")
@PreAuthorize(AuthorizationExpressions.CMS_PAGE_WRITE)
public class PageController {
    private final PageService pageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PageResponse> create(@Valid @RequestBody CreatePageRequest request) {
        return ApiResponse.success(PageResponse.from(pageService.createDraft(
                new CreatePageCommand(request.slug(), request.title(), request.content()))));
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<PageResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.success(PageResponse.from(pageService.getBySlug(slug)));
    }

    @PutMapping("/{id}")
    public ApiResponse<PageResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePageRequest request) {
        return ApiResponse.success(PageResponse.from(pageService.update(
                PageId.of(id), new UpdatePageCommand(request.title(), request.content()))));
    }

    @PostMapping("/{id}/publish")
    public ApiResponse<PageResponse> publish(@PathVariable UUID id) {
        return ApiResponse.success(PageResponse.from(pageService.publish(PageId.of(id))));
    }

    @PostMapping("/{id}/archive")
    public ApiResponse<PageResponse> archive(@PathVariable UUID id) {
        return ApiResponse.success(PageResponse.from(pageService.archive(PageId.of(id))));
    }
}
