package com.hdplatform.shared.domain.valueobject;

import java.io.Serializable;
import java.util.Arrays;

public abstract class BaseValueObject
        implements Serializable {

    protected abstract Object[] equalityValues();

    @Override
    public final boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        BaseValueObject other =
                (BaseValueObject) obj;

        return Arrays.deepEquals(
                equalityValues(),
                other.equalityValues());

    }

    @Override
    public final int hashCode() {
        return Arrays.deepHashCode(
                equalityValues());
    }

}