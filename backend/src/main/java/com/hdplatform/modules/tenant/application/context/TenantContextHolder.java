package com.hdplatform.modules.tenant.application.context;

import java.util.Optional;

/**
 * Holds the tenant for the current synchronous request.
 * Async jobs must carry TenantContext explicitly and must not rely on this holder.
 */
public final class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CURRENT = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void set(TenantContext context) {
        if (CURRENT.get() != null) {
            throw new IllegalStateException("Tenant context is already set for this thread");
        }
        CURRENT.set(context);
    }

    public static Optional<TenantContext> current() {
        return Optional.ofNullable(CURRENT.get());
    }

    public static TenantContext requireCurrent() {
        return current().orElseThrow(() ->
                new IllegalStateException("No tenant is associated with the current request"));
    }

    public static void clear() {
        CURRENT.remove();
    }
}
