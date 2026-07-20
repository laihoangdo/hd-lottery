package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.lottery.domain.valueobject.PrizeTierCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record PrizeTierRequest(
        @NotNull PrizeTierCode code,
        @NotEmpty List<@Pattern(regexp = "[0-9]{2,6}") String> numbers
) {
}
