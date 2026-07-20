package com.hdplatform.modules.platformcatalog.adapter.out;

import com.hdplatform.modules.platformcatalog.application.PlatformCatalogRepository;
import com.hdplatform.modules.platformcatalog.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlatformCatalogPersistenceAdapter implements PlatformCatalogRepository {
    private final VerticalJpaRepository verticals;
    private final WebsiteTemplateJpaRepository templates;

    @Override public Vertical save(Vertical value) { return toDomain(verticals.save(toEntity(value))); }
    @Override public WebsiteTemplate save(WebsiteTemplate value) { return toDomain(templates.save(toEntity(value))); }
    @Override public Optional<Vertical> findVertical(VerticalId id) { return verticals.findById(id.getValue()).map(this::toDomain); }
    @Override public Optional<WebsiteTemplate> findTemplate(TemplateId id) { return templates.findById(id.getValue()).map(this::toDomain); }
    @Override public boolean verticalCodeExists(CatalogCode code) { return verticals.existsByCode(code.value()); }
    @Override public boolean templateCodeExists(CatalogCode code) { return templates.existsByCode(code.value()); }
    @Override public List<Vertical> findAllVerticals() { return verticals.findAll().stream().map(this::toDomain).toList(); }
    @Override public List<WebsiteTemplate> findTemplates(VerticalId id) {
        return templates.findAllByVerticalIdOrderByNameAsc(id.getValue()).stream().map(this::toDomain).toList();
    }

    private VerticalEntity toEntity(Vertical value) {
        VerticalEntity entity = new VerticalEntity();
        entity.setId(value.getId().getValue()); entity.setCode(value.getCode().value()); entity.setName(value.getName());
        entity.setDescription(value.getDescription()); entity.setActive(value.isActive());
        entity.setCreatedAt(value.getCreatedAt()); entity.setUpdatedAt(value.getUpdatedAt()); return entity;
    }
    private Vertical toDomain(VerticalEntity value) {
        return Vertical.restore(VerticalId.of(value.getId()), CatalogCode.of(value.getCode()), value.getName(),
                value.getDescription(), value.isActive(), value.getCreatedAt(), value.getUpdatedAt());
    }
    private WebsiteTemplateEntity toEntity(WebsiteTemplate value) {
        WebsiteTemplateEntity entity = new WebsiteTemplateEntity();
        entity.setId(value.getId().getValue()); entity.setVerticalId(value.getVerticalId().getValue());
        entity.setCode(value.getCode().value()); entity.setName(value.getName());
        entity.setLayoutConfig(value.getLayoutConfig()); entity.setDefaultColors(value.getDefaultColors());
        entity.setActive(value.isActive()); entity.setCreatedAt(value.getCreatedAt()); entity.setUpdatedAt(value.getUpdatedAt());
        return entity;
    }
    private WebsiteTemplate toDomain(WebsiteTemplateEntity value) {
        return WebsiteTemplate.restore(TemplateId.of(value.getId()), VerticalId.of(value.getVerticalId()),
                CatalogCode.of(value.getCode()), value.getName(), value.getLayoutConfig(), value.getDefaultColors(),
                value.isActive(), value.getCreatedAt(), value.getUpdatedAt());
    }
}
