package com.hdplatform.modules.platformcatalog.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlatformCatalogDomainTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void catalogCodesAreStableLowercaseKebabCase() {
        assertThat(CatalogCode.of("lottery-phuong-nghi").value()).isEqualTo("lottery-phuong-nghi");
        assertThatThrownBy(() -> CatalogCode.of("Lottery Classic"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void templateAlwaysBelongsToOneVerticalAndProtectsItsJsonConfig() {
        var layout = json.createObjectNode().put("layoutType", "sidebar-right");
        var template = WebsiteTemplate.create(TemplateId.newId(), VerticalId.newId(),
                CatalogCode.of("lottery-phuong-nghi"), "Phương Nghi", layout,
                json.createObjectNode().put("primary", "#c8102e"), Instant.parse("2026-01-01T00:00:00Z"));

        layout.put("layoutType", "changed-outside");
        assertThat(template.getLayoutConfig().get("layoutType").asText()).isEqualTo("sidebar-right");

        var returned = (com.fasterxml.jackson.databind.node.ObjectNode) template.getLayoutConfig();
        returned.put("layoutType", "changed-return-value");
        assertThat(template.getLayoutConfig().get("layoutType").asText()).isEqualTo("sidebar-right");
    }

    @Test
    void templateConfigurationMustBeAJsonObject() {
        assertThatThrownBy(() -> WebsiteTemplate.create(TemplateId.newId(), VerticalId.newId(),
                CatalogCode.of("lottery-minimal"), "Minimal", json.createArrayNode(),
                json.createObjectNode(), Instant.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("layoutConfig");
    }
}
