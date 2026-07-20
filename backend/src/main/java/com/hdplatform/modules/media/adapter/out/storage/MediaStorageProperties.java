package com.hdplatform.modules.media.adapter.out.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "hd-platform.media")
public record MediaStorageProperties(
        String root,
        long maxSizeBytes,
        Set<String> allowedContentTypes
) {
}
