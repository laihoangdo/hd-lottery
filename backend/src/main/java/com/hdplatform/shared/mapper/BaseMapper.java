package com.hdplatform.shared.mapper;

public interface BaseMapper<D, E> {

    D toDomain(E entity);

    E toEntity(D domain);

}