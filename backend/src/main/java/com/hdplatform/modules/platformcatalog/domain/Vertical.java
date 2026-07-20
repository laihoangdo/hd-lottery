package com.hdplatform.modules.platformcatalog.domain;

import com.hdplatform.shared.domain.AuditableEntity;
import com.hdplatform.shared.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;

public final class Vertical extends AuditableEntity<VerticalId> {
    private final CatalogCode code;
    private String name;
    private String description;
    private boolean active;

    private Vertical(VerticalId id, CatalogCode code, String name, String description, boolean active) {
        super(id);
        this.code = Objects.requireNonNull(code);
        this.name = validName(name);
        this.description = description == null ? null : description.trim();
        this.active = active;
    }

    public static Vertical create(VerticalId id, CatalogCode code, String name, String description, Instant now) {
        Vertical vertical = new Vertical(id, code, name, description, true);
        vertical.markCreated(Objects.requireNonNull(now));
        return vertical;
    }

    public static Vertical restore(VerticalId id, CatalogCode code, String name, String description,
                                   boolean active, Instant createdAt, Instant updatedAt) {
        Vertical vertical = new Vertical(id, code, name, description, active);
        vertical.createdAt = Objects.requireNonNull(createdAt);
        vertical.updatedAt = Objects.requireNonNull(updatedAt);
        return vertical;
    }

    private static String validName(String value) {
        if (value == null || value.isBlank() || value.trim().length() > 150) {
            throw new DomainException("Vertical name must contain between 1 and 150 characters");
        }
        return value.trim();
    }

    public CatalogCode getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
}
