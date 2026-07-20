package com.hdplatform.modules.tenant.domain.aggregate;

import com.hdplatform.modules.platformcatalog.domain.TemplateId;
import com.hdplatform.modules.platformcatalog.domain.VerticalId;
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
import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.domain.rule.BusinessRuleValidator;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate Root of Tenant.
 *
 * <p>
 * A Tenant represents one customer website in HD Platform.
 * </p>
 */
public class Tenant extends AuditableEntity<TenantId>{

    private SiteKey siteKey;

    private DomainName domainName;

    private DisplayName displayName;

    private LogoUrl logo;
    private Hotline hotline;

    private TenantStatus status;

    // private OffsetDateTime updatedAt;

    private TenantName name;

    private TenantCode code;

    private final VerticalId verticalId;
    private TemplateId templateId;


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
	        VerticalId verticalId,
	        TemplateId templateId,
	        Instant createdAt
    ) {
    super(id);

    this.siteKey = Objects.requireNonNull(siteKey);
    this.domainName = Objects.requireNonNull(domainName);
    this.active = true;
    this.displayName = Objects.requireNonNull(displayName);
    this.logo = logo;
    this.hotline = hotline;

    this.name = name;
    this.code = code;
	    this.status = Objects.requireNonNull(status);
	    this.verticalId = Objects.requireNonNull(verticalId);
	    this.templateId = Objects.requireNonNull(templateId);
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
	        VerticalId verticalId,
	        TemplateId templateId,
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

	                verticalId,

	                templateId,
	    
	                createdAt   
        );
    
    }

    public void activate(Instant now) {

        if (status == TenantStatus.ARCHIVED) {
            throw new IllegalStateException(
                    "Archived tenant cannot be activated."
            );
        }
    
        status = TenantStatus.ACTIVE;
        // this.active = true;
        markUpdated(now);
    
    }

    public void deactivate(Instant now) {
        status = TenantStatus.INACTIVE;
        // this.active = false;
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

        this.logo = LogoUrl.of(requireText(logoUrl.toString(), "Logo"));
        // this.logo = logoUrl;

        markUpdated(Instant.now());

    }

    public void changeHotline(Hotline hotline) {

        this.hotline = Hotline.of(requireText(hotline.toString(), "Hotline"));

        markUpdated(Instant.now());

    }

    public void changeDisplayName(DisplayName displayName) {

        this.displayName = DisplayName.of(requireText(displayName.toString(), "DisplayName"));

        markUpdated(Instant.now());

    }

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

	    public VerticalId getVerticalId() { return verticalId; }

	    public TemplateId getTemplateId() { return templateId; }

	    public void switchTemplate(TemplateId templateId, Instant now) {
	        this.templateId = Objects.requireNonNull(templateId, "templateId cannot be null");
	        markUpdated(Objects.requireNonNull(now, "now cannot be null"));
	    }

    private String requireText(
            String value,
            String field
    ) {

        if (value == null || value.isBlank()) {
            throw new DomainException(field + " cannot be blank.");
        }

        return value.trim();

    }


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

	        VerticalId verticalId,

	        TemplateId templateId,

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
	                    verticalId,
	                    templateId,
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
