package com.hdplatform.modules.identity.application.service;

import com.hdplatform.modules.identity.application.auth.*;
import com.hdplatform.modules.identity.application.port.AccessTokenIssuer;
import com.hdplatform.modules.identity.application.port.AuthenticationRepository;
import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.UserStatus;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String INVALID_CREDENTIALS = "Invalid email or password";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AuthenticationRepository repository;
    private final AccessTokenIssuer accessTokenIssuer;
    private final PasswordEncoder passwordEncoder;
    private final ClockProvider clock;

    @Value("${hd-platform.security.refresh-token-ttl:30d}")
    private Duration refreshTokenTtl;

    @Transactional
    public TokenPair login(String email, String rawPassword, AuthenticationScope scope, UUID tenantId) {
        UserCredentials credentials = repository.findCredentialsByEmail(normalizeEmail(email))
                .filter(value -> value.status() == UserStatus.ACTIVE)
                .filter(value -> passwordEncoder.matches(rawPassword, value.passwordHash()))
                .orElseThrow(() -> new DomainException(INVALID_CREDENTIALS));
        return createPair(resolveGrant(credentials.userId(), scope, tenantId), null);
    }

    @Transactional(readOnly = true)
    public AccessTokenIssuer.IssuedAccessToken exchangePlatformIdentity(String email) {
        UserCredentials credentials = repository.findCredentialsByEmail(normalizeEmail(email))
                .filter(value -> value.status() == UserStatus.ACTIVE)
                .orElseThrow(() -> new DomainException("Workspace identity is not authorized"));
        return accessTokenIssuer.issue(resolveGrant(credentials.userId(), AuthenticationScope.PLATFORM, null), clock.now());
    }

    @Transactional
    public TokenPair refresh(String rawRefreshToken) {
        Instant now = clock.now();
        RefreshSession current = repository.findRefreshSessionForUpdate(hash(rawRefreshToken))
                .orElseThrow(() -> new DomainException("Invalid refresh token"));
        if (current.revoked()) {
            repository.revokeFamily(current.familyId(), now);
            throw new DomainException("Refresh token reuse detected");
        }
        if (current.expiredAt(now)) {
            repository.revokeFamily(current.familyId(), now);
            throw new DomainException("Refresh token expired");
        }
        AccessGrant grant = resolveGrant(current.userId(), current.scope(),
                current.tenantId() == null ? null : current.tenantId().getValue());
        return createPair(grant, current);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        repository.findRefreshSessionForUpdate(hash(rawRefreshToken))
                .ifPresent(session -> repository.revokeFamily(session.familyId(), clock.now()));
    }

    private TokenPair createPair(AccessGrant grant, RefreshSession current) {
        Instant now = clock.now();
        AccessTokenIssuer.IssuedAccessToken access = accessTokenIssuer.issue(grant, now);
        String rawRefresh = newRefreshToken();
        UUID familyId = current == null ? UUID.randomUUID() : current.familyId();
        RefreshSession replacement = new RefreshSession(UUID.randomUUID(), familyId, grant.userId(),
                grant.scope(), grant.tenantId(), hash(rawRefresh), now.plus(refreshTokenTtl), null);
        if (current == null) repository.saveRefreshSession(replacement, now);
        else repository.rotateRefreshSession(current.id(), replacement, now);
        return new TokenPair(access.value(), access.expiresAt(), rawRefresh, replacement.expiresAt());
    }

    private AccessGrant resolveGrant(UserId userId, AuthenticationScope scope, UUID tenantId) {
        if (scope == AuthenticationScope.PLATFORM) {
            if (tenantId != null) throw new DomainException("tenantId is not allowed for platform login");
            return repository.findPlatformGrant(userId)
                    .orElseThrow(() -> new DomainException("Access is not available for requested scope"));
        }
        if (tenantId == null) throw new DomainException("tenantId is required for tenant login");
        return repository.findTenantGrant(userId, TenantId.of(tenantId))
                .orElseThrow(() -> new DomainException("Access is not available for requested scope"));
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String newRefreshToken() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest((token == null ? "" : token).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
