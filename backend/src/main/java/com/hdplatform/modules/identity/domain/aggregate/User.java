package com.hdplatform.modules.identity.domain.aggregate;

import com.hdplatform.modules.identity.domain.valueobject.Email;
import com.hdplatform.modules.identity.domain.valueobject.FullName;
import com.hdplatform.modules.identity.domain.valueobject.Password;
import com.hdplatform.modules.identity.domain.valueobject.UserStatus;
import com.hdplatform.shared.domain.AuditableEntity;

import java.time.Instant;
import java.util.Objects;

public final class User extends AuditableEntity<UserId> {
    private final Email email;
    private FullName fullName;
    private Password password;
    private UserStatus status;

    private User(UserId id, Email email, FullName fullName, Password password) {
        super(id);
        this.email = Objects.requireNonNull(email);
        this.fullName = Objects.requireNonNull(fullName);
        this.password = Objects.requireNonNull(password);
        this.status = UserStatus.PENDING;
    }

    public static User register(UserId id, Email email, FullName fullName,
                                Password password, Instant now) {
        User user = new User(id, email, fullName, password);
        user.markCreated(Objects.requireNonNull(now));
        return user;
    }

    public void activate(Instant now) {
        status = UserStatus.ACTIVE;
        markUpdated(Objects.requireNonNull(now));
    }

    public void lock(Instant now) {
        status = UserStatus.LOCKED;
        markUpdated(Objects.requireNonNull(now));
    }

    public Email getEmail() { return email; }
    public FullName getFullName() { return fullName; }
    public Password getPassword() { return password; }
    public UserStatus getStatus() { return status; }
}
