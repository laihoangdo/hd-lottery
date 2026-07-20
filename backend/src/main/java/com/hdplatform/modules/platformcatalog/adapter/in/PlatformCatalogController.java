package com.hdplatform.modules.platformcatalog.adapter.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.hdplatform.modules.platformcatalog.application.PlatformCatalogService;
import com.hdplatform.modules.platformcatalog.domain.*;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/platform/catalog")
@PreAuthorize(AuthorizationExpressions.PLATFORM_CATALOG_MANAGE)
public class PlatformCatalogController {
    private final PlatformCatalogService service;

    @PostMapping("/verticals") @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VerticalResponse> createVertical(@Valid @RequestBody CreateVerticalRequest request) {
        return ApiResponse.success(VerticalResponse.from(service.createVertical(request.code(), request.name(), request.description())));
    }
    @GetMapping("/verticals")
    public ApiResponse<List<VerticalResponse>> verticals() {
        return ApiResponse.success(service.verticals().stream().map(VerticalResponse::from).toList());
    }
    @PostMapping("/verticals/{verticalId}/templates") @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TemplateResponse> createTemplate(@PathVariable UUID verticalId,
                                                         @Valid @RequestBody CreateTemplateRequest request) {
        return ApiResponse.success(TemplateResponse.from(service.createTemplate(VerticalId.of(verticalId), request.code(),
                request.name(), request.layoutConfig(), request.defaultColors())));
    }
    @GetMapping("/verticals/{verticalId}/templates")
    public ApiResponse<List<TemplateResponse>> templates(@PathVariable UUID verticalId) {
        return ApiResponse.success(service.templates(VerticalId.of(verticalId)).stream().map(TemplateResponse::from).toList());
    }

    public record CreateVerticalRequest(@NotBlank String code, @NotBlank String name, String description) {}
    public record CreateTemplateRequest(@NotBlank String code, @NotBlank String name,
                                        @NotNull JsonNode layoutConfig, @NotNull JsonNode defaultColors) {}
    public record VerticalResponse(UUID id, String code, String name, String description, boolean active) {
        static VerticalResponse from(Vertical value) { return new VerticalResponse(value.getId().getValue(), value.getCode().value(),
                value.getName(), value.getDescription(), value.isActive()); }
    }
    public record TemplateResponse(UUID id, UUID verticalId, String code, String name,
                                   JsonNode layoutConfig, JsonNode defaultColors, boolean active) {
        static TemplateResponse from(WebsiteTemplate value) { return new TemplateResponse(value.getId().getValue(),
                value.getVerticalId().getValue(), value.getCode().value(), value.getName(), value.getLayoutConfig(),
                value.getDefaultColors(), value.isActive()); }
    }
}
