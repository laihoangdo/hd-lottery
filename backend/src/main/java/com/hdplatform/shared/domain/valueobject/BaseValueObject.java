package com.hdplatform.shared.domain.valueobject;

import java.util.Objects;

public abstract class BaseValueObject {

    protected abstract Object[] getEqualityComponents();

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        BaseValueObject other = (BaseValueObject) obj;

        return Objects.deepEquals(
                getEqualityComponents(),
                other.getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEqualityComponents());
    }

}