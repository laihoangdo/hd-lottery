package com.hdplatform.modules.tenant.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;

/**
 * JPA entity representing a tenant record.
 *
 * <p>
 * This class belongs to the Infrastructure layer.
 * It must never be used directly inside the Domain layer.
 * </p>
 */
@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
public class TenantEntity {

    // @Id
    // @Column(name = "id", nullable = false, updatable = false)
    // private UUID id;

    // @Column(name = "site_key", nullable = false, unique = true, length = 50)
    // private String siteKey;

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String siteKey;

    @Column(nullable = false, unique = true)
    private String domainName;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String code;

    private String logoUrl;

    private String hotline;

    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    @Column(nullable = false)
    private UUID verticalId;

    @Column(nullable = false)
    private UUID templateId;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;
}
