package com.hdplatform.modules.lottery.domain.valueobject;

import java.util.List;
import java.util.Objects;

public record PrizeTier(PrizeTierCode code, List<WinningNumber> numbers) {
    public PrizeTier {
        Objects.requireNonNull(code);
        numbers = List.copyOf(numbers);
        if (numbers.isEmpty()) throw new IllegalArgumentException("Prize tier cannot be empty");
    }
}
