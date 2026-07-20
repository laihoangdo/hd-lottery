package com.hdplatform.modules.platformcatalog.application;

import com.hdplatform.modules.platformcatalog.domain.*;

import java.util.List;
import java.util.Optional;

public interface PlatformCatalogRepository {
    Vertical save(Vertical vertical);
    WebsiteTemplate save(WebsiteTemplate template);
    Optional<Vertical> findVertical(VerticalId id);
    Optional<WebsiteTemplate> findTemplate(TemplateId id);
    boolean verticalCodeExists(CatalogCode code);
    boolean templateCodeExists(CatalogCode code);
    List<Vertical> findAllVerticals();
    List<WebsiteTemplate> findTemplates(VerticalId verticalId);
}
