package com.hdplatform.shared.authorization;

public final class AuthorizationExpressions {
    public static final String CMS_PAGE_WRITE =
            "@tenantAuthorization.hasTenantPermission(authentication, '"
                    + PermissionCatalog.CMS_PAGE_WRITE + "')";
    public static final String PLATFORM_TENANT_MANAGE =
            "@tenantAuthorization.hasPlatformPermission(authentication, '"
                    + PermissionCatalog.PLATFORM_TENANT_MANAGE + "')";
    public static final String PLATFORM_CATALOG_MANAGE =
            "@tenantAuthorization.hasPlatformPermission(authentication, '"
                    + PermissionCatalog.PLATFORM_CATALOG_MANAGE + "')";
    public static final String MEDIA_ASSET_WRITE =
            "@tenantAuthorization.hasTenantPermission(authentication, '"
                    + PermissionCatalog.MEDIA_ASSET_WRITE + "')";
    public static final String REPORTING_DASHBOARD_READ =
            "@tenantAuthorization.hasTenantPermission(authentication, '"
                    + PermissionCatalog.REPORTING_DASHBOARD_READ + "')";
    public static final String ANALYTICS_DASHBOARD_READ =
            "@tenantAuthorization.hasTenantPermission(authentication, '"
                    + PermissionCatalog.ANALYTICS_DASHBOARD_READ + "')";
    public static final String LOTTERY_RESULT_MANAGE =
            "@tenantAuthorization.hasPlatformPermission(authentication, '"
                    + PermissionCatalog.LOTTERY_RESULT_MANAGE + "')";
    public static final String TENANT_TEMPLATE_SWITCH =
            "@tenantAuthorization.hasTenantPermission(authentication, '"
                    + PermissionCatalog.TENANT_TEMPLATE_SWITCH + "')";

    private AuthorizationExpressions() {
    }
}
