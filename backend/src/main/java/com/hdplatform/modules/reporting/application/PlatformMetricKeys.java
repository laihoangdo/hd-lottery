package com.hdplatform.modules.reporting.application;

import com.hdplatform.modules.reporting.domain.valueobject.MetricKey;

public final class PlatformMetricKeys {
    public static final MetricKey CMS_PAGE_TOTAL = MetricKey.of("cms.page.total");
    public static final MetricKey CMS_PAGE_DRAFT = MetricKey.of("cms.page.draft");
    public static final MetricKey CMS_PAGE_PUBLISHED = MetricKey.of("cms.page.published");
    public static final MetricKey CMS_PAGE_ARCHIVED = MetricKey.of("cms.page.archived");
    public static final MetricKey MEDIA_ASSET_TOTAL = MetricKey.of("media.asset.total");
    public static final MetricKey MEDIA_ASSET_PRIVATE = MetricKey.of("media.asset.private");
    public static final MetricKey MEDIA_ASSET_PUBLIC = MetricKey.of("media.asset.public");

    private PlatformMetricKeys() {
    }
}
