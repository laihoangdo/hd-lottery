package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateLotteryResultRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9_]{2,20}") String provinceCode,
        @NotBlank @Size(max = 100) String provinceName,
        @NotNull LotteryRegion region,
        @NotNull LocalDate drawDate,
        @NotNull List<@Valid PrizeTierRequest> prizes
) {
}
