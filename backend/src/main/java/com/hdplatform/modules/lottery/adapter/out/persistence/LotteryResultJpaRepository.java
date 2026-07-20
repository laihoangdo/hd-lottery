package com.hdplatform.modules.lottery.adapter.out.persistence;

import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.util.Optional;

public interface LotteryResultJpaRepository extends JpaRepository<LotteryResultEntity, UUID> {
    boolean existsByProvinceCodeAndDrawDate(String provinceCode, LocalDate drawDate);
    List<LotteryResultEntity> findByDrawDateAndRegionAndStatusOrderByProvinceName(
            LocalDate drawDate, LotteryRegion region, LotteryResultStatus status);
    List<LotteryResultEntity> findByDrawDateAndRegionAndStatusInOrderByProvinceName(
            LocalDate drawDate, LotteryRegion region, Collection<LotteryResultStatus> statuses);
    Optional<LotteryResultEntity> findByProvinceCodeAndDrawDateAndStatus(
            String provinceCode, LocalDate drawDate, LotteryResultStatus status);
}
