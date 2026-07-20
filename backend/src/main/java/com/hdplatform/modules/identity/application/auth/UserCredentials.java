package com.hdplatform.modules.identity.application.auth;

import com.hdplatform.modules.identity.domain.aggregate.UserId;
import com.hdplatform.modules.identity.domain.valueobject.UserStatus;

public record UserCredentials(
        UserId userId,
        String email,
        String passwordHash,
        UserStatus status
) {}
