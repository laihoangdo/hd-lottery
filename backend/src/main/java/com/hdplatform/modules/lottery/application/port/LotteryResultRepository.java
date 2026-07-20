package com.hdplatform.modules.lottery.application.port;

import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultId;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotteryResultRepository {
    LotteryResult save(LotteryResult result);
    Optional<LotteryResult> findById(LotteryResultId id);
    boolean existsByProvinceAndDate(String provinceCode, LocalDate drawDate);
    List<LotteryResult> findPublished(LocalDate drawDate, LotteryRegion region);
    List<LotteryResult> findLive(LocalDate drawDate, LotteryRegion region);
    Optional<LotteryResult> findPublishedByProvinceAndDate(
            String provinceCode, LocalDate drawDate);
}
