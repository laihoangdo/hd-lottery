package com.hdplatform.modules.cms.application.service;

import com.hdplatform.modules.cms.application.command.CreatePageCommand;
import com.hdplatform.modules.cms.application.command.UpdatePageCommand;
import com.hdplatform.modules.cms.application.port.PageRepository;
import com.hdplatform.modules.cms.domain.aggregate.Page;
import com.hdplatform.modules.cms.domain.aggregate.PageId;
import com.hdplatform.modules.cms.domain.valueobject.PageSlug;
import com.hdplatform.modules.cms.domain.valueobject.PageStatus;
import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.exception.ConflictException;
import com.hdplatform.shared.exception.NotFoundException;
import com.hdplatform.modules.reporting.application.PlatformMetricKeys;
import com.hdplatform.modules.reporting.application.service.TenantMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final ClockProvider clockProvider;
    private final TenantMetrics tenantMetrics;

    @Transactional
    public Page createDraft(CreatePageCommand command) {
        TenantContext tenant = TenantContextHolder.requireCurrent();
        PageSlug slug = PageSlug.of(command.slug());
        if (pageRepository.existsBySlug(tenant.tenantId(), slug)) {
            throw new ConflictException("CMS_PAGE_SLUG_EXISTS", "Page slug already exists");
        }
        Page saved = pageRepository.save(Page.createDraft(
                PageId.newId(), tenant.tenantId(), slug,
                command.title(), command.content(), clockProvider.now()));
        tenantMetrics.increment(tenant.tenantId(), PlatformMetricKeys.CMS_PAGE_TOTAL, 1);
        tenantMetrics.increment(tenant.tenantId(), PlatformMetricKeys.CMS_PAGE_DRAFT, 1);
        return saved;
    }

    @Transactional(readOnly = true)
    public Page getBySlug(String slug) {
        TenantContext tenant = TenantContextHolder.requireCurrent();
        return pageRepository.findBySlug(tenant.tenantId(), PageSlug.of(slug))
                .orElseThrow(() -> new NotFoundException(
                        "CMS_PAGE_NOT_FOUND", "Page not found"));
    }

    @Transactional(readOnly = true)
    public Page getPublishedBySlug(String slug) {
        TenantContext tenant = TenantContextHolder.requireCurrent();
        return pageRepository.findBySlugAndStatus(
                        tenant.tenantId(), PageSlug.of(slug), PageStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        "CMS_PAGE_NOT_FOUND", "Page not found"));
    }

    @Transactional
    public Page update(PageId pageId, UpdatePageCommand command) {
        Page page = currentTenantPage(pageId);
        page.update(command.title(), command.content(), clockProvider.now());
        return pageRepository.save(page);
    }

    @Transactional
    public Page publish(PageId pageId) {
        Page page = currentTenantPage(pageId);
        PageStatus previous = page.getStatus();
        page.publish(clockProvider.now());
        Page saved = pageRepository.save(page);
        if (previous != PageStatus.PUBLISHED) {
            tenantMetrics.increment(page.getTenantId(), PlatformMetricKeys.CMS_PAGE_DRAFT, -1);
            tenantMetrics.increment(page.getTenantId(), PlatformMetricKeys.CMS_PAGE_PUBLISHED, 1);
        }
        return saved;
    }

    @Transactional
    public Page archive(PageId pageId) {
        Page page = currentTenantPage(pageId);
        PageStatus previous = page.getStatus();
        page.archive(clockProvider.now());
        Page saved = pageRepository.save(page);
        if (previous != PageStatus.ARCHIVED) {
            tenantMetrics.increment(page.getTenantId(),
                    previous == PageStatus.PUBLISHED
                            ? PlatformMetricKeys.CMS_PAGE_PUBLISHED
                            : PlatformMetricKeys.CMS_PAGE_DRAFT,
                    -1);
            tenantMetrics.increment(page.getTenantId(), PlatformMetricKeys.CMS_PAGE_ARCHIVED, 1);
        }
        return saved;
    }

    private Page currentTenantPage(PageId pageId) {
        TenantContext tenant = TenantContextHolder.requireCurrent();
        return pageRepository.findById(tenant.tenantId(), pageId)
                .orElseThrow(() -> new NotFoundException(
                        "CMS_PAGE_NOT_FOUND", "Page not found"));
    }
}
