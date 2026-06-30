package com.hdplatform.modules.tenant.domain.valueobject;

/**
 * Current status of a Tenant.
 */
public enum TenantStatus {

    /**
     * Tenant is active and can serve requests.
     */
    ACTIVE,

    /**
     * Tenant is temporarily disabled.
     */
    INACTIVE,

    /**
     * Tenant is suspended by administrator.
     */
    SUSPENDED,

    /**
     * Tenant has been archived (soft delete).
     */
    ARCHIVED

}