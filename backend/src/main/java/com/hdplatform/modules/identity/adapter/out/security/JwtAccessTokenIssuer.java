package com.hdplatform.modules.identity.adapter.out.security;

import com.hdplatform.modules.identity.application.auth.AccessGrant;
import com.hdplatform.modules.identity.application.auth.AuthenticationScope;
import com.hdplatform.modules.identity.application.port.AccessTokenIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAccessTokenIssuer implements AccessTokenIssuer {
    private final JwtEncoder encoder;

    @Value("${hd-platform.security.jwt.issuer}") private String issuer;
    @Value("${hd-platform.security.jwt.audience}") private String audience;
    @Value("${hd-platform.security.jwt.key-id:hd-platform-1}") private String keyId;
    @Value("${hd-platform.security.jwt.access-token-ttl:15m}") private Duration ttl;

    @Override public IssuedAccessToken issue(AccessGrant grant, Instant now) {
        Instant expiresAt = now.plus(ttl);
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder().issuer(issuer).subject(grant.userId().getValue().toString())
                .audience(List.of(audience)).issuedAt(now).expiresAt(expiresAt)
                .claim("scope", grant.scope().name().toLowerCase())
                .claim("roles", grant.roles()).claim("permissions", grant.permissions());
        if (grant.scope() == AuthenticationScope.TENANT) claims.claim("tenant_id", grant.tenantId().getValue().toString());
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).keyId(keyId).build();
        return new IssuedAccessToken(encoder.encode(JwtEncoderParameters.from(header, claims.build())).getTokenValue(), expiresAt);
    }
}
