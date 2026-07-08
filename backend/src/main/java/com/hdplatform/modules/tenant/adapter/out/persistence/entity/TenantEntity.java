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

    // @Column(name = "domain_name", nullable = false, unique = true, length = 255)
    // private String domainName;

    // @Column(name = "display_name", nullable = false, length = 100)
    // private String displayName;

    // @Column(name = "logo", length = 500)
    // private String logo;

    // @Column(name = "hotline", length = 30)
    // private String hotline;

    // @Column(name = "status", nullable = false, length = 30)
    // private String status;

    // @Column(name = "created_at", nullable = false)
    // private OffsetDateTime createdAt;

    // @Column(name = "updated_at", nullable = false)
    // private OffsetDateTime updatedAt;

    // protected TenantEntity() {
    //     // Required by JPA
    // }

    // public UUID getId() {
    //     return id;
    // }

    // public void setId(UUID id) {
    //     this.id = id;
    // }

    // public String getSiteKey() {
    //     return siteKey;
    // }

    // public void setSiteKey(String siteKey) {
    //     this.siteKey = siteKey;
    // }

    // public String getDomainName() {
    //     return domainName;
    // }

    // public void setDomainName(String domainName) {
    //     this.domainName = domainName;
    // }

    // public String getDisplayName() {
    //     return displayName;
    // }

    // public void setDisplayName(String displayName) {
    //     this.displayName = displayName;
    // }

    // public String getLogo() {
    //     return logo;
    // }

    // public void setLogo(String logo) {
    //     this.logo = logo;
    // }

    // public String getHotline() {
    //     return hotline;
    // }

    // public void setHotline(String hotline) {
    //     this.hotline = hotline;
    // }

    // public String getStatus() {
    //     return status;
    // }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

    // public OffsetDateTime getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(OffsetDateTime createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public OffsetDateTime getUpdatedAt() {
    //     return updatedAt;
    // }

    // public void setUpdatedAt(OffsetDateTime updatedAt) {
    //     this.updatedAt = updatedAt;
    // }
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
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;
}