package com.hdplatform.modules.tenant.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTenantRequest(

    @NotBlank
    String displayName,

    String logoUrl,

    String hotline

) {
}