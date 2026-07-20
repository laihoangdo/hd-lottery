package com.hdplatform.modules.identity.application.service;

import com.hdplatform.modules.identity.application.port.RoleSummaryRepository;
import com.hdplatform.modules.identity.application.query.RoleSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleCatalogService {
    private final RoleSummaryRepository roles;

    @Transactional(readOnly = true)
    public List<RoleSummary> platformRoles() {
        return roles.findPlatformRoles();
    }
}
