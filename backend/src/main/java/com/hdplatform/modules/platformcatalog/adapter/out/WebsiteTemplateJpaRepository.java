package com.hdplatform.modules.platformcatalog.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface WebsiteTemplateJpaRepository extends JpaRepository<WebsiteTemplateEntity, UUID> {
    boolean existsByCode(String code);
    List<WebsiteTemplateEntity> findAllByVerticalIdOrderByNameAsc(UUID verticalId);
}
