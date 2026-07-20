package com.hdplatform.modules.cms.domain.aggregate;

import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageTest {

    private static final Instant CREATED_AT = Instant.parse("2026-07-20T00:00:00Z");

    @Test
    void publishes_content_and_records_publication_time() {
        Page page = draft("Content");
        Instant publishedAt = CREATED_AT.plusSeconds(60);

        page.publish(publishedAt);

        assertThat(page.getStatus()).isEqualTo(PageStatus.PUBLISHED);
        assertThat(page.getPublishedAt()).isEqualTo(publishedAt);
        assertThat(page.getUpdatedAt()).isEqualTo(publishedAt);
    }

    @Test
    void refuses_to_publish_empty_content() {
        Page page = draft("   ");

        assertThatThrownBy(() -> page.publish(CREATED_AT.plusSeconds(60)))
                .isInstanceOf(DomainException.class);
        assertThat(page.getStatus()).isEqualTo(PageStatus.DRAFT);
    }

    @Test
    void archived_page_is_immutable() {
        Page page = draft("Content");
        page.archive(CREATED_AT.plusSeconds(60));

        assertThatThrownBy(() -> page.update(
                "Changed", "Changed", CREATED_AT.plusSeconds(120)))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> page.publish(CREATED_AT.plusSeconds(120)))
                .isInstanceOf(DomainException.class);
    }

    private Page draft(String content) {
        return Page.createDraft(PageId.newId(), TenantId.newId(),
                PageSlug.of("about-us"), "About us", content, CREATED_AT);
    }
}
