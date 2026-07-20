package com.hdplatform.modules.lottery.application.command;

import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;

import java.time.LocalDate;
import java.util.List;

public record CreateLotteryResultCommand(
        String provinceCode,
        String provinceName,
        LotteryRegion region,
        LocalDate drawDate,
        List<PrizeTierCommand> prizes
) {
}
