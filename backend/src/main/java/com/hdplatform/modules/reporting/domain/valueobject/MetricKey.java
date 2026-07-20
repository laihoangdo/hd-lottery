package com.hdplatform.modules.reporting.domain.valueobject;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record MetricKey(String value) {
    private static final Pattern PATTERN =
            Pattern.compile("^[a-z][a-z0-9]*(?:\\.[a-z][a-z0-9]*){2,4}$");

    public MetricKey {
        Objects.requireNonNull(value, "metric key cannot be null");
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.length() > 100 || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid metric key");
        }
    }

    public static MetricKey of(String value) { return new MetricKey(value); }
}
