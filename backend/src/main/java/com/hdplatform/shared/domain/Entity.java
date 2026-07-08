package com.hdplatform.shared.domain;

import java.util.Objects;

public abstract class Entity<ID extends Identifier<?>> {

    protected ID id;

    protected Entity() {
    }

    protected Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        Entity<?> entity = (Entity<?>) o;

        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}