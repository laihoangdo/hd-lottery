package com.hdplatform.modules.identity.adapter.in.rest;

import com.hdplatform.modules.identity.application.query.RoleSummary;
import com.hdplatform.modules.identity.application.service.RoleCatalogService;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/platform/identity")
public class PlatformIdentityController {
    private final RoleCatalogService roles;

    @GetMapping("/roles")
    @PreAuthorize("@tenantAuthorization.hasPlatformPermission(authentication, 'platform:identity:manage')")
    public ApiResponse<List<RoleSummary>> roles() {
        return ApiResponse.success(roles.platformRoles());
    }
}
