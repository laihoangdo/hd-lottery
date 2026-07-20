package com.hdplatform.modules.analytics.application;

import com.hdplatform.modules.analytics.domain.valueobject.AnalyticsEventName;

public final class AnalyticsEvents {
    public static final AnalyticsEventName PAGE_VIEW = AnalyticsEventName.of("page_view");
    public static final AnalyticsEventName MEDIA_DOWNLOAD = AnalyticsEventName.of("media_download");
    public static final AnalyticsEventName LOTTERY_TICKET_CHECK =
            AnalyticsEventName.of("lottery_ticket_check");
    public static final AnalyticsEventName LOTTERY_RESULT_VIEW =
            AnalyticsEventName.of("lottery_result_view");

    private static final java.util.Map<String, AnalyticsEventName> CATALOG =
            java.util.stream.Stream.of(
                    PAGE_VIEW, MEDIA_DOWNLOAD, LOTTERY_TICKET_CHECK, LOTTERY_RESULT_VIEW)
                    .collect(java.util.stream.Collectors.toUnmodifiableMap(
                            AnalyticsEventName::value, event -> event));

    public static AnalyticsEventName requireKnown(String value) {
        AnalyticsEventName normalized = AnalyticsEventName.of(value);
        AnalyticsEventName known = CATALOG.get(normalized.value());
        if (known == null) throw new com.hdplatform.shared.exception.ValidationException(
                "ANALYTICS_EVENT_UNKNOWN", "Unknown analytics event name");
        return known;
    }

    private AnalyticsEvents() {
    }
}
