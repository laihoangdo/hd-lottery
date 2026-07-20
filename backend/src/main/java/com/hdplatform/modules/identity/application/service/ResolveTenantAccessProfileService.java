package com.hdplatform.modules.identity.application.service;

import com.hdplatform.modules.identity.application.port.TenantAccessProfileRepository;
import com.hdplatform.modules.identity.application.query.TenantAccessProfile;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResolveTenantAccessProfileService {
    private final TenantAccessProfileRepository repository;

    @Transactional(readOnly = true)
    public TenantAccessProfile resolve(UserId userId, TenantId tenantId) {
        return repository.findActive(userId, tenantId)
                .orElseThrow(() -> new NotFoundException(
                        "ACTIVE_MEMBERSHIP_NOT_FOUND",
                        "Active tenant membership not found"));
    }
}
