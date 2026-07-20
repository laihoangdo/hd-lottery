package com.hdplatform.modules.platformcatalog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdplatform.modules.platformcatalog.domain.*;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlatformCatalogServiceTest {
    private final Instant now = Instant.parse("2026-01-01T00:00:00Z");
    private final InMemoryCatalog repository = new InMemoryCatalog();
    private PlatformCatalogService service;

    @BeforeEach
    void setUp() {
        ClockProvider clock = mock(ClockProvider.class);
        when(clock.now()).thenReturn(now);
        service = new PlatformCatalogService(repository, clock);
    }

    @Test
    void createsTemplateOnlyForAnExistingActiveVertical() {
        Vertical vertical = service.createVertical("lottery", "Xổ số", null);
        WebsiteTemplate template = service.createTemplate(vertical.getId(), "lottery-phuong-nghi", "Phương Nghi",
                new ObjectMapper().createObjectNode(), new ObjectMapper().createObjectNode());

        assertThat(template.getVerticalId()).isEqualTo(vertical.getId());
        assertThat(service.templates(vertical.getId())).containsExactly(template);
    }

    @Test
    void rejectsTemplateForUnknownVertical() {
        assertThatThrownBy(() -> service.createTemplate(VerticalId.newId(), "lottery-modern", "Modern",
                new ObjectMapper().createObjectNode(), new ObjectMapper().createObjectNode()))
                .isInstanceOf(DomainException.class).hasMessage("Vertical not found");
    }

    @Test
    void codesAreUniqueAcrossTheirCatalog() {
        service.createVertical("lottery", "Xổ số", null);
        assertThatThrownBy(() -> service.createVertical("lottery", "Lottery duplicate", null))
                .isInstanceOf(DomainException.class).hasMessage("Vertical code already exists");
    }

    private static final class InMemoryCatalog implements PlatformCatalogRepository {
        private final Map<VerticalId, Vertical> verticals = new LinkedHashMap<>();
        private final Map<TemplateId, WebsiteTemplate> templates = new LinkedHashMap<>();
        public Vertical save(Vertical value) { verticals.put(value.getId(), value); return value; }
        public WebsiteTemplate save(WebsiteTemplate value) { templates.put(value.getId(), value); return value; }
        public Optional<Vertical> findVertical(VerticalId id) { return Optional.ofNullable(verticals.get(id)); }
        public Optional<WebsiteTemplate> findTemplate(TemplateId id) { return Optional.ofNullable(templates.get(id)); }
        public boolean verticalCodeExists(CatalogCode code) { return verticals.values().stream().anyMatch(v -> v.getCode().equals(code)); }
        public boolean templateCodeExists(CatalogCode code) { return templates.values().stream().anyMatch(v -> v.getCode().equals(code)); }
        public List<Vertical> findAllVerticals() { return List.copyOf(verticals.values()); }
        public List<WebsiteTemplate> findTemplates(VerticalId id) { return templates.values().stream().filter(t -> t.getVerticalId().equals(id)).toList(); }
    }
}
