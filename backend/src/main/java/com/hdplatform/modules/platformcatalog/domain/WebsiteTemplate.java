package com.hdplatform.modules.platformcatalog.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.hdplatform.shared.domain.AuditableEntity;
import com.hdplatform.shared.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;

public final class WebsiteTemplate extends AuditableEntity<TemplateId> {
    private final VerticalId verticalId;
    private final CatalogCode code;
    private String name;
    private JsonNode layoutConfig;
    private JsonNode defaultColors;
    private boolean active;

    private WebsiteTemplate(TemplateId id, VerticalId verticalId, CatalogCode code, String name,
                            JsonNode layoutConfig, JsonNode defaultColors, boolean active) {
        super(id);
        this.verticalId = Objects.requireNonNull(verticalId);
        this.code = Objects.requireNonNull(code);
        this.name = validName(name);
        this.layoutConfig = objectNode(layoutConfig, "layoutConfig");
        this.defaultColors = objectNode(defaultColors, "defaultColors");
        this.active = active;
    }

    public static WebsiteTemplate create(TemplateId id, VerticalId verticalId, CatalogCode code, String name,
                                         JsonNode layoutConfig, JsonNode defaultColors, Instant now) {
        WebsiteTemplate template = new WebsiteTemplate(id, verticalId, code, name, layoutConfig, defaultColors, true);
        template.markCreated(Objects.requireNonNull(now));
        return template;
    }

    public static WebsiteTemplate restore(TemplateId id, VerticalId verticalId, CatalogCode code, String name,
                                          JsonNode layoutConfig, JsonNode defaultColors, boolean active,
                                          Instant createdAt, Instant updatedAt) {
        WebsiteTemplate template = new WebsiteTemplate(id, verticalId, code, name, layoutConfig, defaultColors, active);
        template.createdAt = Objects.requireNonNull(createdAt);
        template.updatedAt = Objects.requireNonNull(updatedAt);
        return template;
    }

    private static String validName(String value) {
        if (value == null || value.isBlank() || value.trim().length() > 150) {
            throw new DomainException("Template name must contain between 1 and 150 characters");
        }
        return value.trim();
    }

    private static JsonNode objectNode(JsonNode value, String field) {
        if (value == null || !value.isObject()) throw new DomainException(field + " must be a JSON object");
        return value.deepCopy();
    }

    public VerticalId getVerticalId() { return verticalId; }
    public CatalogCode getCode() { return code; }
    public String getName() { return name; }
    public JsonNode getLayoutConfig() { return layoutConfig.deepCopy(); }
    public JsonNode getDefaultColors() { return defaultColors.deepCopy(); }
    public boolean isActive() { return active; }
}
