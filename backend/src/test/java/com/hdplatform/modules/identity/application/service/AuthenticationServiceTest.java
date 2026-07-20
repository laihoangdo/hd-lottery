package com.hdplatform.modules.identity.application.service;

import com.hdplatform.modules.identity.application.auth.*;
import com.hdplatform.modules.identity.application.port.AccessTokenIssuer;
import com.hdplatform.modules.identity.application.port.AuthenticationRepository;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.UserStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    private final AuthenticationRepository repository = mock(AuthenticationRepository.class);
    private final AccessTokenIssuer issuer = mock(AccessTokenIssuer.class);
    private final PasswordEncoder passwords = mock(PasswordEncoder.class);
    private final ClockProvider clock = mock(ClockProvider.class);
    private AuthenticationService service;
    private final Instant now = Instant.parse("2026-07-21T00:00:00Z");
    private final UserId userId = UserId.newId();

    @BeforeEach void setUp() {
        service = new AuthenticationService(repository, issuer, passwords, clock);
        ReflectionTestUtils.setField(service, "refreshTokenTtl", Duration.ofDays(30));
        when(clock.now()).thenReturn(now);
        when(issuer.issue(any(), eq(now))).thenReturn(new AccessTokenIssuer.IssuedAccessToken("access", now.plusSeconds(900)));
    }

    @Test void logsInToTenantAndPersistsOnlyHashedRefreshToken() {
        UUID tenant = UUID.randomUUID();
        when(repository.findCredentialsByEmail("owner@example.com")).thenReturn(Optional.of(
                new UserCredentials(userId, "owner@example.com", "$2a$12$hash", UserStatus.ACTIVE)));
        when(passwords.matches("secret", "$2a$12$hash")).thenReturn(true);
        when(repository.findTenantGrant(userId, TenantId.of(tenant))).thenReturn(Optional.of(
                new AccessGrant(userId, AuthenticationScope.TENANT, TenantId.of(tenant), Set.of("TENANT_OWNER"), Set.of("cms:page:write"))));

        TokenPair result = service.login(" Owner@Example.com ", "secret", AuthenticationScope.TENANT, tenant);

        assertThat(result.accessToken()).isEqualTo("access");
        assertThat(result.refreshToken()).hasSize(64);
        ArgumentCaptor<RefreshSession> saved = ArgumentCaptor.forClass(RefreshSession.class);
        verify(repository).saveRefreshSession(saved.capture(), eq(now));
        assertThat(saved.getValue().tokenHash()).hasSize(64).isNotEqualTo(result.refreshToken());
    }

    @Test void rejectsWrongPasswordWithoutResolvingGrant() {
        when(repository.findCredentialsByEmail("user@example.com")).thenReturn(Optional.of(
                new UserCredentials(userId, "user@example.com", "hash", UserStatus.ACTIVE)));
        when(passwords.matches(anyString(), anyString())).thenReturn(false);
        assertThatThrownBy(() -> service.login("user@example.com", "wrong", AuthenticationScope.PLATFORM, null))
                .isInstanceOf(DomainException.class).hasMessage("Invalid email or password");
        verify(repository, never()).findPlatformGrant(any());
    }

    @Test void exchangesAuthorizedWorkspaceIdentityForPlatformAccessToken() {
        UserCredentials credentials = new UserCredentials(userId, "admin@example.com", "unused", UserStatus.ACTIVE);
        AccessGrant grant = new AccessGrant(userId, AuthenticationScope.PLATFORM, null,
                Set.of("super_admin"), Set.of("platform:tenant:manage"));
        when(repository.findCredentialsByEmail("admin@example.com")).thenReturn(Optional.of(credentials));
        when(repository.findPlatformGrant(userId)).thenReturn(Optional.of(grant));

        AccessTokenIssuer.IssuedAccessToken token = service.exchangePlatformIdentity(" Admin@Example.com ");

        assertThat(token.value()).isEqualTo("access");
        verify(issuer).issue(grant, now);
        verifyNoInteractions(passwords);
    }

    @Test void refreshRotatesTokenAndReloadsCurrentGrant() {
        UUID tenant = UUID.randomUUID(); UUID family = UUID.randomUUID();
        RefreshSession current = new RefreshSession(UUID.randomUUID(), family, userId, AuthenticationScope.TENANT,
                TenantId.of(tenant), "stored-hash", now.plusSeconds(60), null);
        when(repository.findRefreshSessionForUpdate(anyString())).thenReturn(Optional.of(current));
        AccessGrant grant = new AccessGrant(userId, AuthenticationScope.TENANT, TenantId.of(tenant), Set.of("EDITOR"), Set.of("cms:page:write"));
        when(repository.findTenantGrant(userId, TenantId.of(tenant))).thenReturn(Optional.of(grant));

        TokenPair result = service.refresh("old-token");

        assertThat(result.refreshToken()).isNotEqualTo("old-token");
        ArgumentCaptor<RefreshSession> replacement = ArgumentCaptor.forClass(RefreshSession.class);
        verify(repository).rotateRefreshSession(eq(current.id()), replacement.capture(), eq(now));
        assertThat(replacement.getValue().familyId()).isEqualTo(family);
        verify(issuer).issue(grant, now);
    }

    @Test void revokedRefreshTokenRevokesWholeFamilyAsReuseAttack() {
        UUID family = UUID.randomUUID();
        RefreshSession revoked = new RefreshSession(UUID.randomUUID(), family, userId, AuthenticationScope.PLATFORM,
                null, "hash", now.plusSeconds(60), now.minusSeconds(1));
        when(repository.findRefreshSessionForUpdate(anyString())).thenReturn(Optional.of(revoked));

        assertThatThrownBy(() -> service.refresh("reused-token"))
                .isInstanceOf(DomainException.class).hasMessage("Refresh token reuse detected");
        verify(repository).revokeFamily(family, now);
        verifyNoInteractions(issuer);
    }
}
