package com.hdplatform.modules.tenant.adapter.in.rest.controller;

import com.hdplatform.modules.tenant.adapter.in.rest.mapper.TenantRestMapper;
import com.hdplatform.modules.tenant.adapter.in.rest.request.CreateTenantRequest;
import com.hdplatform.modules.tenant.adapter.in.rest.response.TenantResponse;
import com.hdplatform.modules.tenant.application.usecase.CreateTenantService;
import com.hdplatform.modules.tenant.domain.aggregate.Tenant;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tenants")
public class TenantController {

    private final CreateTenantService handler;

    private final TenantRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TenantResponse> create(

            @Valid
            @RequestBody
            CreateTenantRequest request

    ) {

        Tenant tenant = handler.execute(
                mapper.toCommand(request));

        return ApiResponse.success(
                mapper.toResponse(tenant));

    }

}