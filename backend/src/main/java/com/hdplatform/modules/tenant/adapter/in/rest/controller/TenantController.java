package com.hdplatform.modules.tenant.adapter.in.rest.controller;

import com.hdplatform.modules.tenant.adapter.in.rest.mapper.TenantRestMapper;
import com.hdplatform.modules.tenant.adapter.in.rest.request.CreateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.response.TenantResponse;
import com.hdplatform.modules.tenant.application.usecase.CreateTenantService;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Tenant",
        description = "Tenant Management APIs"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tenants")
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

}