package com.hdplatform.shared.domain;

import java.io.Serializable;

public interface Identifier<T> extends Serializable {

    T getValue();

}