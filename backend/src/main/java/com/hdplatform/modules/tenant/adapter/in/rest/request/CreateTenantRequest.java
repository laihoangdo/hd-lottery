package com.hdplatform.modules.tenant.adapter.in.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

        @NotBlank(message = "Site key is required")
        @Size(min = 3, max = 100,
                message = "Site key must be between 3 and 100 characters")
        @Pattern(
                regexp = "^[a-z0-9-]+$",
                message = "Site key only allows lowercase letters, numbers and hyphens")
        @Schema(example = "hdlottery")
        String siteKey,

        @NotBlank(message = "Domain name is required")
        @Size(max = 255)
        @Schema(example = "hdlottery.vn")
        String domainName,

        @NotBlank(message = "Display name is required")
        @Size(max = 255)
        @Schema(example = "HD Lottery")
        String displayName,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String code,

        @Size(max = 500)
        String logoUrl,

        @Size(max = 30)
        String hotline

) {
}