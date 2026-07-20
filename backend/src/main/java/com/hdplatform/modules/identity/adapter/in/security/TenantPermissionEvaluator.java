package com.hdplatform.modules.identity.adapter.in.security;

import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("tenantAuthorization")
public class TenantPermissionEvaluator {

    public boolean hasTenantPermission(Authentication authentication, String permission) {
        if (!hasPermission(authentication, permission) || !hasScope(authentication, "tenant")) {
            return false;
        }
        return TenantContextHolder.current()
                .map(context -> tenantClaimMatches(authentication, context.tenantId().getValue()))
                .orElse(false);
    }

    public boolean hasPlatformPermission(Authentication authentication, String permission) {
        return hasPermission(authentication, permission) && hasScope(authentication, "platform");
    }

    private boolean hasPermission(Authentication authentication, String permission) {
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permission));
    }

    private boolean tenantClaimMatches(Authentication authentication, UUID tenantId) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return false;
        }
        String claim = jwt.getClaimAsString("tenant_id");
        try {
            return claim != null && tenantId.equals(UUID.fromString(claim));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean hasScope(Authentication authentication, String expectedScope) {
        return authentication != null
                && authentication.getPrincipal() instanceof Jwt jwt
                && expectedScope.equals(jwt.getClaimAsString("scope"));
    }
}
