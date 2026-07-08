package com.hdplatform.shared.domain.rule;

public interface BusinessRule {

    boolean isBroken();

    String message();

}