package com.hdplatform.modules.tenant.adapter.in.rest.response;

import java.time.Instant;
import java.util.UUID;

public record TenantResponse(

        UUID id,

        String siteKey,

        String domainName,

        String displayName,

        String logoUrl,

        String hotline,

        String status,

        UUID verticalId,

        UUID templateId,

        Instant createdAt,

        Instant updatedAt

){}

    
