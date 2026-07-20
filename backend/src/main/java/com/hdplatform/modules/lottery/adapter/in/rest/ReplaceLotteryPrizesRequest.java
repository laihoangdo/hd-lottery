package com.hdplatform.modules.lottery.adapter.in.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record ReplaceLotteryPrizesRequest(
        @PositiveOrZero long expectedVersion,
        @NotNull List<@Valid PrizeTierRequest> prizes
) {
}
