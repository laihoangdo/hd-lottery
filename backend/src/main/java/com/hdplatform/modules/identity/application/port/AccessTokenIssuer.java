package com.hdplatform.modules.identity.application.port;

import com.hdplatform.modules.identity.application.auth.AccessGrant;

import java.time.Instant;

public interface AccessTokenIssuer {
    IssuedAccessToken issue(AccessGrant grant, Instant now);

    record IssuedAccessToken(String value, Instant expiresAt) {}
}
