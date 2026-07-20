package com.hdplatform.modules.tenant.adapter.in.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import java.util.UUID;

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
        String hotline,

        @NotNull(message = "Vertical is required")
        UUID verticalId,

        @NotNull(message = "Template is required")
        UUID templateId,

        @NotBlank(message = "Owner email is required")
        @Email(message = "Owner email is invalid")
        @Size(max = 320)
        String ownerEmail,

        @NotBlank(message = "Owner full name is required")
        @Size(max = 100)
        String ownerFullName,

        @NotBlank(message = "Initial password is required")
        @Size(min = 12, max = 72, message = "Initial password must be between 12 and 72 characters")
        String ownerInitialPassword

) {
}
