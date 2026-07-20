package com.hdplatform.modules.tenant.adapter.in.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hd-platform.multitenancy")
public record TenantResolutionProperties(boolean siteKeyHeaderEnabled) {
}
