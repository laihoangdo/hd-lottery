package com.hdplatform.modules.media.application.service;

import java.util.Set;

public record MediaPolicy(long maxSizeBytes, Set<String> allowedContentTypes) {
    public MediaPolicy {
        if (maxSizeBytes <= 0) throw new IllegalArgumentException("maxSizeBytes must be positive");
        allowedContentTypes = Set.copyOf(allowedContentTypes);
    }
}
