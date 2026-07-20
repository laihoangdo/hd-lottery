package com.hdplatform.modules.tenant.application.query;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record SiteConfiguration(
        UUID tenantId,
        String siteKey,
        String domain,
        String displayName,
        String logoUrl,
        String hotline,
        String verticalCode,
        String templateCode,
        JsonNode layoutConfig,
        JsonNode colors
) {}
