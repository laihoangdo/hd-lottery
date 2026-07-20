package com.hdplatform.modules.platformcatalog.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.hdplatform.modules.platformcatalog.domain.*;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformCatalogService {
    private final PlatformCatalogRepository repository;
    private final ClockProvider clock;

    @Transactional
    public Vertical createVertical(String code, String name, String description) {
        CatalogCode catalogCode = CatalogCode.of(code);
        if (repository.verticalCodeExists(catalogCode)) throw new DomainException("Vertical code already exists");
        return repository.save(Vertical.create(VerticalId.newId(), catalogCode, name, description, clock.now()));
    }

    @Transactional
    public WebsiteTemplate createTemplate(VerticalId verticalId, String code, String name,
                                          JsonNode layoutConfig, JsonNode defaultColors) {
        Vertical vertical = repository.findVertical(verticalId)
                .orElseThrow(() -> new DomainException("Vertical not found"));
        if (!vertical.isActive()) throw new DomainException("Cannot add a template to an inactive vertical");
        CatalogCode catalogCode = CatalogCode.of(code);
        if (repository.templateCodeExists(catalogCode)) throw new DomainException("Template code already exists");
        return repository.save(WebsiteTemplate.create(TemplateId.newId(), verticalId, catalogCode, name,
                layoutConfig, defaultColors, clock.now()));
    }

    @Transactional(readOnly = true)
    public List<Vertical> verticals() { return repository.findAllVerticals(); }

    @Transactional(readOnly = true)
    public List<WebsiteTemplate> templates(VerticalId verticalId) { return repository.findTemplates(verticalId); }
}
