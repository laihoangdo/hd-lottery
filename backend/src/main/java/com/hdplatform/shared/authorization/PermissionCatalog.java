package com.hdplatform.shared.authorization;

/** Stable permission identifiers shared by token issuance and API authorization. */
public final class PermissionCatalog {
    public static final String CMS_PAGE_WRITE = "cms:page:write";
    public static final String PLATFORM_TENANT_MANAGE = "platform:tenant:manage";
    public static final String PLATFORM_CATALOG_MANAGE = "platform:catalog:manage";
    public static final String PLATFORM_IDENTITY_MANAGE = "platform:identity:manage";
    public static final String MEDIA_ASSET_WRITE = "media:asset:write";
    public static final String REPORTING_DASHBOARD_READ = "reporting:dashboard:read";
    public static final String ANALYTICS_DASHBOARD_READ = "analytics:dashboard:read";
    public static final String LOTTERY_RESULT_MANAGE = "lottery:result:manage";
    public static final String TENANT_TEMPLATE_SWITCH = "tenant:template:switch";

    private PermissionCatalog() {
    }
}
