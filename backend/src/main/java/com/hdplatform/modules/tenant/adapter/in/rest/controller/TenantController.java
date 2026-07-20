package com.hdplatform.modules.tenant.adapter.in.rest.controller;

import com.hdplatform.modules.tenant.adapter.in.rest.mapper.TenantRestMapper;
import com.hdplatform.modules.tenant.adapter.in.rest.request.CreateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.request.UpdateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.response.TenantResponse;
import com.hdplatform.modules.tenant.application.query.GetTenantQuery;
import com.hdplatform.modules.tenant.application.usecase.CreateTenantService;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@Tag(
        name = "Tenant",
        description = "Tenant Management APIs"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tenants")
@PreAuthorize(AuthorizationExpressions.PLATFORM_TENANT_MANAGE)
public class TenantController {

    private final CreateTenantService handler;

    private final TenantRestMapper mapper;

        @Operation(
                summary = "Create tenant",
                description = "Create a new tenant."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "201",
                        description = "Created"
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Tenant already exists"
                )
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public com.hdplatform.shared.response.ApiResponse<TenantResponse> create(

                @Valid
                @RequestBody
                CreateTenantRequest request

        ) {

                Tenant tenant = handler.execute(
                        mapper.toCommand(request));

                return com.hdplatform.shared.response.ApiResponse.success(
                        mapper.toResponse(tenant));

        }

        @GetMapping("/{id}")
        public com.hdplatform.shared.response.ApiResponse<TenantResponse> getById(
                @PathVariable UUID id
        ) {

        Tenant tenant = handler.getById(
                new GetTenantQuery(
                        TenantId.of(id)));

        return com.hdplatform.shared.response.ApiResponse.success(
                mapper.toResponse(tenant));

        }

        @GetMapping
        public com.hdplatform.shared.response.ApiResponse<List<TenantResponse>> findAll() {
               return  com.hdplatform.shared.response.ApiResponse.success(handler.getAll()
                .stream()
                .map(mapper::toResponse)
                .toList());

        }

        @PutMapping("/{id}")
        public com.hdplatform.shared.response.ApiResponse<TenantResponse> putMethodName(@PathVariable UUID id, @Valid @RequestBody UpdateTenantRequest request) {
                Tenant tenant = handler.update(TenantId.of(id), mapper.toUpdateCommand(request));

                return com.hdplatform.shared.response.ApiResponse.success(
                        mapper.toResponse(tenant));

        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(
                @PathVariable UUID id
        ) {

        handler.delete(
                TenantId.of(id));

        }

        @PutMapping("/{id}/template")
        public com.hdplatform.shared.response.ApiResponse<TenantResponse> switchTemplate(
                @PathVariable UUID id,
                @Valid @RequestBody SwitchTemplateRequest request) {
                return com.hdplatform.shared.response.ApiResponse.success(mapper.toResponse(
                        handler.switchTemplate(TenantId.of(id), TemplateId.of(request.templateId()))));
        }

        public record SwitchTemplateRequest(@NotNull UUID templateId) {}

}
