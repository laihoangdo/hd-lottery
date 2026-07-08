package com.hdplatform.modules.tenant.domain.aggregate;

import com.hdplatform.modules.tenant.domain.event.TenantCreatedEvent;
import com.hdplatform.modules.tenant.domain.rule.TenantCodeMustNotBeEmptyRule;
import com.hdplatform.modules.tenant.domain.rule.TenantNameMustNotBeEmptyRule;
import com.hdplatform.modules.tenant.domain.valueobject.DisplayName;
import com.hdplatform.modules.tenant.domain.valueobject.DomainName;
import com.hdplatform.modules.tenant.domain.valueobject.Hotline;
import com.hdplatform.modules.tenant.domain.valueobject.LogoUrl;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.modules.tenant.domain.valueobject.TenantCode;
import com.hdplatform.modules.tenant.domain.valueobject.TenantName;
import com.hdplatform.modules.tenant.domain.valueobject.TenantStatus;
import com.hdplatform.shared.domain.AuditableEntity;
import com.hdplatform.shared.domain.rule.BusinessRuleValidator;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate Root of Tenant.
 *
 * <p>
 * A Tenant represents one lottery dealer website in HD Platform.
 * </p>
 */
public class Tenant extends AuditableEntity<TenantId>{

    // private final TenantId id;

    // private final OffsetDateTime createdAt;

    private SiteKey siteKey;

    private DomainName domainName;

    private DisplayName displayName;

    private LogoUrl logo;
    private Hotline hotline;

    private TenantStatus status;

    // private OffsetDateTime updatedAt;

    private TenantName name;

    private TenantCode code;


    private boolean active;

    private Tenant(
        TenantId id,
        SiteKey siteKey,
        DomainName domainName,
        DisplayName displayName,
        TenantName name,
        TenantCode code,
        LogoUrl logo,
        Hotline hotline,
        TenantStatus status,
        Instant createdAt
    ) {
    super(id);

    this.siteKey = Objects.requireNonNull(siteKey);
    this.domainName = Objects.requireNonNull(domainName);
    this.active = true;

    // Fix NPE: displayName must be initialized
    this.displayName = Objects.requireNonNull(displayName);

    // Keep nullable fields consistent with persistence mapper
    this.logo = logo;
    this.hotline = hotline;

    this.name = name;
    this.code = code;
    this.status = Objects.requireNonNull(status);
    }

    public static Tenant restore(
        TenantId id,
        SiteKey siteKey,
        DomainName domainName,
        DisplayName displayName,
        TenantName name,
        TenantCode code,
        LogoUrl logo,
        Hotline hotline,
        TenantStatus status,
        Instant createdAt,
        Instant updatedAt
    
    ) {
    
        return new Tenant(
    
                id,
    
                siteKey,
    
                domainName,
    
                displayName,
                
                name,
                code,
                logo,
    
                hotline,
    
                status,
    
                createdAt   
        );
    
    }

    public void activate(Instant now) {

        if (status == TenantStatus.ARCHIVED) {
            throw new IllegalStateException(
                    "Archived tenant cannot be activated."
            );
        }
    
        // status = TenantStatus.ACTIVE;
        // touch();
        this.active = true;

        markUpdated(now);
    
    }

    public void deactivate(Instant now) {

        // status = TenantStatus.INACTIVE;

        // touch();
        this.active = false;

        markUpdated(now);

    }

    public void suspend() {

        if (status == TenantStatus.ARCHIVED) {
    
            throw new IllegalStateException(
    
                    "Archived tenant cannot be suspended."
    
            );
    
        }
    
        status = TenantStatus.SUSPENDED;
    
        markUpdated(Instant.now());
    
    }

    // clockProvider.now()
    public void archive(Instant now) {

        if (status == TenantStatus.ARCHIVED) {
    
            return;
    
        }
    
        status = TenantStatus.ARCHIVED;
    
        markUpdated(now);
    
    }
    public void changeLogo(LogoUrl logoUrl) {

        // this.logo = requireText(logoUrl.toString(), "Logo");
        this.logo = logoUrl;

        markUpdated(Instant.now());

    }

    public void changeHotline(Hotline hotline) {

        this.hotline = hotline;

        markUpdated(Instant.now());

    }

    public void changeDisplayName(DisplayName displayName) {

        this.displayName = displayName;

        markUpdated(Instant.now());

    }

    // public TenantId getId() {
    //     return id;
    // }

    public SiteKey getSiteKey() {
        return siteKey;
    }

    public DomainName getDomainName() {
        return domainName;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public LogoUrl getLogoUrl() {
        return logo;
    }

    public Hotline getHotline() {
        return hotline;
    }

    public TenantStatus getStatus() {
        return status;
    }

    // private void touch() {
    //     updatedAt = OffsetDateTime.now();
    // }

    // private String requireText(
    //         String value,
    //         String field
    // ) {

    //     if (value == null || value.isBlank()) {
    //         throw new IllegalArgumentException(field + " cannot be blank.");
    //     }

    //     return value.trim();

    // }


    public static Tenant register(
        TenantId id,
        SiteKey siteKey,
        TenantName name,
        TenantCode code,

        DomainName domainName,

        DisplayName displayName,

        LogoUrl logo,

        Hotline hotline,

        TenantStatus status,

        Instant now
    ) {

        BusinessRuleValidator.check(
                new TenantNameMustNotBeEmptyRule(
                        name.value()));

        BusinessRuleValidator.check(
                new TenantCodeMustNotBeEmptyRule(
                        code.value()));

        Tenant tenant =
                new Tenant(
                        id,
                    siteKey,
                    domainName,
                    displayName,
                    name,
                    code,
                    logo,
                    hotline,
                    status,
                    now);

        tenant.markCreated(now);

        tenant.registerEvent(
                new TenantCreatedEvent(
                        tenant.getId(),
                        now));

        return tenant;
    }

    public TenantName getName() {
        return name;
    }

    public TenantCode getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }

}