package com.hdplatform.modules.tenant.adapter.out.persistence.repository;

import com.hdplatform.modules.tenant.adapter.out.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Tenant persistence.
 */
public interface TenantJpaRepository extends JpaRepository<TenantEntity, UUID> {

    Optional<TenantEntity> findBySiteKey(String siteKey);

    Optional<TenantEntity> findByDomainName(String domainName);

    boolean existsBySiteKey(String siteKey);

    boolean existsByDomainName(String domainName);

    Optional<TenantEntity> findByCode(String code);

    boolean existsByCode(String code);

}