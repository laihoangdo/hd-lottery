package com.hdplatform.modules.tenant.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

        @NotBlank
        @Size(max = 100)
        String siteKey,

        @NotBlank
        @Size(max = 255)
        String domainName,

        @NotBlank
        @Size(max = 255)
        String displayName,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String code,


        String logoUrl,

        String hotline

) {
}