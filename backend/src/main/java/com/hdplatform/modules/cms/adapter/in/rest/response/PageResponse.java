package com.hdplatform.modules.cms.adapter.in.rest.response;

import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;

import java.time.Instant;
import java.util.UUID;

public record PageResponse(UUID id, String slug, String title, String content,
                           PageStatus status, Instant publishedAt,
                           Instant createdAt, Instant updatedAt) {
    public static PageResponse from(Page page) {
        return new PageResponse(page.getId().getValue(), page.getSlug().value(),
                page.getTitle(), page.getContent(), page.getStatus(), page.getPublishedAt(),
                page.getCreatedAt(), page.getUpdatedAt());
    }
}
