package com.hdplatform.modules.cms.domain.aggregate;

import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.AuditableEntity;
import com.hdplatform.shared.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;

public final class Page extends AuditableEntity<PageId> {

    private final TenantId tenantId;
    private PageSlug slug;
    private String title;
    private String content;
    private PageStatus status;
    private Instant publishedAt;

    private Page(PageId id, TenantId tenantId, PageSlug slug, String title,
                 String content, PageStatus status, Instant publishedAt) {
        super(id);
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.slug = Objects.requireNonNull(slug, "slug cannot be null");
        this.title = validTitle(title);
        this.content = Objects.requireNonNull(content, "content cannot be null");
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.publishedAt = publishedAt;
    }

    public static Page createDraft(PageId id, TenantId tenantId, PageSlug slug,
                                   String title, String content, Instant now) {
        Page page = new Page(id, tenantId, slug, title, content, PageStatus.DRAFT, null);
        page.markCreated(Objects.requireNonNull(now, "now cannot be null"));
        return page;
    }

    public static Page restore(PageId id, TenantId tenantId, PageSlug slug,
                               String title, String content, PageStatus status,
                               Instant publishedAt, Instant createdAt, Instant updatedAt) {
        Page page = new Page(id, tenantId, slug, title, content, status, publishedAt);
        page.createdAt = Objects.requireNonNull(createdAt);
        page.updatedAt = Objects.requireNonNull(updatedAt);
        return page;
    }

    public void update(String title, String content, Instant now) {
        ensureNotArchived();
        this.title = validTitle(title);
        this.content = Objects.requireNonNull(content, "content cannot be null");
        markUpdated(Objects.requireNonNull(now, "now cannot be null"));
    }

    public void publish(Instant now) {
        ensureNotArchived();
        if (content.isBlank()) {
            throw new DomainException("A page must have content before it can be published");
        }
        if (status != PageStatus.PUBLISHED) {
            status = PageStatus.PUBLISHED;
            publishedAt = Objects.requireNonNull(now, "now cannot be null");
            markUpdated(now);
        }
    }

    public void archive(Instant now) {
        if (status != PageStatus.ARCHIVED) {
            status = PageStatus.ARCHIVED;
            markUpdated(Objects.requireNonNull(now, "now cannot be null"));
        }
    }

    private void ensureNotArchived() {
        if (status == PageStatus.ARCHIVED) {
            throw new DomainException("Archived page cannot be changed");
        }
    }

    private static String validTitle(String title) {
        if (title == null || title.isBlank() || title.trim().length() > 200) {
            throw new DomainException("Page title must contain between 1 and 200 characters");
        }
        return title.trim();
    }

    public TenantId getTenantId() { return tenantId; }
    public PageSlug getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public PageStatus getStatus() { return status; }
    public Instant getPublishedAt() { return publishedAt; }
}
