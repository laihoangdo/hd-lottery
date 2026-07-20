package com.hdplatform.modules.identity.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record PermissionCode(String value) {
    private static final Pattern PATTERN =
            Pattern.compile("^[a-z][a-z0-9-]*(?::[a-z][a-z0-9-]*){2}$");

    public PermissionCode {
        Objects.requireNonNull(value, "permission code cannot be null");
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.length() > 100 || !PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid permission code");
        }
    }

    public static PermissionCode of(String value) {
        return new PermissionCode(value);
    }
}
