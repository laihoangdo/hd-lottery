package com.hdplatform.modules.cms.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record PageSlug(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    public PageSlug {
        Objects.requireNonNull(value, "Page slug cannot be null");
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.length() > 160 || !PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid page slug");
        }
    }

    public static PageSlug of(String value) {
        return new PageSlug(value);
    }
}
