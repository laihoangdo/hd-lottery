package com.hdplatform.modules.platformcatalog.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface VerticalJpaRepository extends JpaRepository<VerticalEntity, UUID> {
    boolean existsByCode(String code);
}
