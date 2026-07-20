package com.hdplatform.modules.platformcatalog.domain;

import com.hdplatform.shared.domain.exception.DomainException;

import java.util.Locale;

public record CatalogCode(String value) {
    public CatalogCode {
        if (value == null || !value.matches("[a-z0-9]+(?:-[a-z0-9]+)*")) {
            throw new DomainException("Catalog code must be lowercase kebab-case");
        }
        value = value.toLowerCase(Locale.ROOT);
    }

    public static CatalogCode of(String value) { return new CatalogCode(value == null ? null : value.trim()); }
}
