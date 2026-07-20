package com.hdplatform.modules.identity.application.service;

import com.hdplatform.modules.identity.application.port.TenantAccessProfileRepository;
import com.hdplatform.modules.identity.application.query.TenantAccessProfile;
import com.hdplatform.modules.identity.domain.aggregate.TenantMembershipId;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResolveTenantAccessProfileServiceTest {

    private final TenantAccessProfileRepository repository =
            mock(TenantAccessProfileRepository.class);
    private final ResolveTenantAccessProfileService service =
            new ResolveTenantAccessProfileService(repository);

    @Test
    void resolves_profile_only_for_requested_user_and_tenant() {
        UserId userId = UserId.newId();
        TenantId tenantId = TenantId.newId();
        TenantAccessProfile expected = new TenantAccessProfile(
                userId, TenantMembershipId.newId(), tenantId, Set.of(), Set.of());
        when(repository.findActive(userId, tenantId)).thenReturn(Optional.of(expected));

        assertThat(service.resolve(userId, tenantId)).isSameAs(expected);
        verify(repository).findActive(userId, tenantId);
    }

    @Test
    void refuses_switch_to_tenant_without_active_membership() {
        UserId userId = UserId.newId();
        TenantId tenantId = TenantId.newId();
        when(repository.findActive(userId, tenantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resolve(userId, tenantId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Active tenant membership not found");
    }
}
