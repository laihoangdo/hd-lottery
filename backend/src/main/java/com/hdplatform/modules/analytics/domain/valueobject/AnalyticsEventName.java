package com.hdplatform.modules.analytics.domain.valueobject;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record AnalyticsEventName(String value) {
    private static final Pattern PATTERN = Pattern.compile("^[a-z][a-z0-9_]{2,49}$");

    public AnalyticsEventName {
        Objects.requireNonNull(value, "event name cannot be null");
        value = value.trim().toLowerCase(Locale.ROOT);
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid analytics event name");
        }
    }

    public static AnalyticsEventName of(String value) { return new AnalyticsEventName(value); }
}
