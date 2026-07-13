package com.hdplatform.modules.tenant.adapter.in.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

        @NotBlank
        @Size(max = 100)
        @Schema(example = "hdlottery")
        String siteKey,

        @NotBlank
        @Size(max = 255)
        @Schema(example = "hdlottery.vn")
        String domainName,

        @NotBlank
        @Size(max = 255)
        @Schema(example = "HD Lottery")
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