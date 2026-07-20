package com.hdplatform.modules.lottery.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public record WinningNumber(String value) {
    private static final Pattern PATTERN = Pattern.compile("^[0-9]{2,6}$");

    public WinningNumber {
        Objects.requireNonNull(value, "winning number cannot be null");
        value = value.trim();
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Winning number must contain 2 to 6 digits");
        }
    }

    public static WinningNumber of(String value) { return new WinningNumber(value); }
}
