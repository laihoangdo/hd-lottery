package com.hdplatform.modules.tenant.adapter.in.web;

import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.application.service.TenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TenantResolutionProperties.class)
public class TenantResolutionFilter extends OncePerRequestFilter {

    static final String SITE_KEY_HEADER = "X-HD-Tenant";

    private final TenantResolver tenantResolver;
    private final TenantResolutionProperties properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            resolve(request).ifPresent(TenantContextHolder::set);
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

    private Optional<TenantContext> resolve(HttpServletRequest request) {
        String siteKey = request.getHeader(SITE_KEY_HEADER);
        if (properties.siteKeyHeaderEnabled() && siteKey != null && !siteKey.isBlank()) {
            return tenantResolver.resolveBySiteKey(siteKey);
        }
        return tenantResolver.resolveByDomain(request.getServerName());
    }
}
