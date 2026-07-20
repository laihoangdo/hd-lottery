package com.hdplatform.modules.identity.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record RoleCode(String value) {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z][A-Z0-9_]{2,49}$");

    public RoleCode {
        Objects.requireNonNull(value, "role code cannot be null");
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid role code");
        }
    }

    public static RoleCode of(String value) {
        return new RoleCode(value);
    }
}
