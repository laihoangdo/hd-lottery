package com.hdplatform.modules.identity.domain.valueobject;

import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.domain.valueobject.BaseValueObject;

/**
 * Avatar URL.
 */
public final class Avatar extends BaseValueObject {

    private final String value;

    private Avatar(String value) {
        this.value = value;
    }

    public static Avatar of(String value) {

        if (value == null || value.isBlank()) {
            return new Avatar(null);
        }

        String normalized = value.trim();

        if (normalized.length() > 500) {
            throw new DomainException("Avatar url is too long.");
        }

        return new Avatar(normalized);

    }

    public String value() {
        return value;
    }

    @Override
    protected Object[] equalityValues() {
        return new Object[]{value};
    }

}