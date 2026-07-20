package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.valueobject.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record LotteryResultResponse(
        UUID id,
        String provinceCode,
        String provinceName,
        LotteryRegion region,
        LocalDate drawDate,
        List<PrizeResponse> prizes,
        LotteryResultStatus status,
        Instant publishedAt,
        long version
) {
    static LotteryResultResponse from(LotteryResult result) {
        return new LotteryResultResponse(result.getId().getValue(), result.getProvinceCode(),
                result.getProvinceName(), result.getRegion(), result.getDrawDate(),
                result.getPrizes().stream().map(PrizeResponse::from).toList(),
                result.getStatus(), result.getPublishedAt(), result.getVersion());
    }

    public record PrizeResponse(PrizeTierCode code, List<String> numbers) {
        static PrizeResponse from(PrizeTier prize) {
            return new PrizeResponse(prize.code(),
                    prize.numbers().stream().map(WinningNumber::value).toList());
        }
    }
}
