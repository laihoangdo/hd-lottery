package com.hdplatform.modules.tenant.domain.aggregate;

import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import com.hdplatform.modules.tenant.domain.valueobject.Logo;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Aggregate Root of Tenant.
 *
 * <p>
 * A Tenant represents one lottery dealer website in HD Platform.
 * </p>
 */
public final class Tenant {

    private final TenantId id;

    private final OffsetDateTime createdAt;

    private SiteKey siteKey;

    private DomainName domainName;

    private String displayName;

    private String logoUrl;

    private String hotline;

    private TenantStatus status;

    private OffsetDateTime updatedAt;

    private Tenant(
        TenantId id,
        SiteKey siteKey,
        DomainName domainName,
        DisplayName displayName,
        Logo logo,
        Hotline hotline,
        TenantStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    this.id = Objects.requireNonNull(id);

    this.siteKey = Objects.requireNonNull(siteKey);

    this.domainName = Objects.requireNonNull(domainName);

    // this.displayName = Objects.requireNonNull(displayName);

    // this.logoUrl = logo;

    // this.hotline = hotline;

    this.status = Objects.requireNonNull(status);

    this.createdAt = Objects.requireNonNull(createdAt);

    this.updatedAt = Objects.requireNonNull(updatedAt);

}

        public static Tenant create(
            SiteKey siteKey,
            DomainName domainName,
            DisplayName displayName
        ) {

        OffsetDateTime now = OffsetDateTime.now();

        return new Tenant(

                TenantId.newId(),

                siteKey,

                domainName,

                displayName,

                null,

                null,

                TenantStatus.ACTIVE,

                now,

                now

        );

        }

        public static Tenant restore(

            TenantId id,
    
            SiteKey siteKey,
    
            DomainName domainName,
    
            DisplayName displayName,
    
            Logo logo,
    
            Hotline hotline,
    
            TenantStatus status,
    
            OffsetDateTime createdAt,
    
            OffsetDateTime updatedAt
    
    ) {
    
        return new Tenant(
    
                id,
    
                siteKey,
    
                domainName,
    
                displayName,
    
                logo,
    
                hotline,
    
                status,
    
                createdAt,
    
                updatedAt
    
        );
    
    }

    public void activate() {

        if (status == TenantStatus.ARCHIVED) {
            throw new IllegalStateException(
                    "Archived tenant cannot be activated."
            );
        }
    
        status = TenantStatus.ACTIVE;
    
        touch();
    
    }

    public void deactivate() {

        status = TenantStatus.INACTIVE;

        touch();

    }

    public void suspend() {

        if (status == TenantStatus.ARCHIVED) {
    
            throw new IllegalStateException(
    
                    "Archived tenant cannot be suspended."
    
            );
    
        }
    
        status = TenantStatus.SUSPENDED;
    
        touch();
    
    }

    public void archive() {

        if (status == TenantStatus.ARCHIVED) {
    
            return;
    
        }
    
        status = TenantStatus.ARCHIVED;
    
        touch();
    
    }
    public void changeLogo(String logoUrl) {

        this.logoUrl = requireText(logoUrl, "Logo");

        touch();

    }

    public void changeHotline(String hotline) {

        this.hotline = requireText(hotline, "Hotline");

        touch();

    }

    public void changeDisplayName(String displayName) {

        this.displayName = requireText(displayName, "Display name");

        touch();

    }

    public TenantId getId() {
        return id;
    }

    public SiteKey getSiteKey() {
        return siteKey;
    }

    public DomainName getDomainName() {
        return domainName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getHotline() {
        return hotline;
    }

    public TenantStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void touch() {
        updatedAt = OffsetDateTime.now();
    }

    private String requireText(
            String value,
            String field
    ) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank.");
        }

        return value.trim();

    }

}